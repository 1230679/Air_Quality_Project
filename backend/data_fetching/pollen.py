from .config import API_KEY
from .models import PollenData
import requests
from django.utils.dateparse import parse_datetime
from django.utils import timezone

class Pollen:
    def __init__(self):
        self.baseUrl = "https://pollen.googleapis.com/v1"

    def fill_db(self, location, response):
        # Accept either a Location model instance or a primary key
        from .models import Location as LocationModel
        if hasattr(location, 'id'):
            location_obj = location
        else:
            try:
                location_obj = LocationModel.objects.get(pk=location)
            except Exception:
                print(f"Invalid location provided to fill_db: {location}")
                return

        # Support responses that either have a top-level 'pollen' key
        data = response.get('pollen') if isinstance(response, dict) and 'pollen' in response else response
        daily_infos = data.get('dailyInfo', []) if isinstance(data, dict) else []

        from datetime import datetime
        for daily_info in daily_infos:
            date = daily_info.get('date')
            if not date:
                continue

            # date is expected as dict {year, month, day}
            if isinstance(date, dict):
                try:
                    year = int(date.get('year'))
                    month = int(date.get('month'))
                    day = int(date.get('day'))
                    dt = datetime(year, month, day)
                except Exception:
                    # skip invalid date
                    continue
            else:
                # try parsing ISO string
                try:
                    dt = parse_datetime(str(date))
                    if dt is None:
                        from datetime import datetime as _dt
                        dt = _dt.fromisoformat(str(date))
                except Exception:
                    continue

            # make timezone-aware if naive
            if timezone.is_naive(dt):
                try:
                    dt = timezone.make_aware(dt)
                except Exception:
                    # fallback to now
                    dt = timezone.now()

            pollen_type_infos = daily_info.get('pollenTypeInfo', []) or []
            for pollen_type_info in pollen_type_infos:
                display_name = pollen_type_info.get('displayName') or pollen_type_info.get('code')
                index_info = pollen_type_info.get('indexInfo') or {}
                index_value = None
                index_display_name = ''
                if isinstance(index_info, dict):
                    index_value = index_info.get('value')
                    index_display_name = index_info.get('displayName') or index_info.get('category') or ''

                # Ensure index_value is a float because model does not allow null
                try:
                    index_value = float(index_value) if index_value is not None else 0.0
                except Exception:
                    index_value = 0.0

                lookup = {
                    'location': location_obj,
                    'timestamp': dt,
                    'pollen_type': display_name,
                }
                defaults = {
                    'index_value': index_value,
                    'index_display_name': index_display_name,
                }

                try:
                    PollenData.objects.update_or_create(defaults=defaults, **lookup)
                except Exception as e:
                    print(f"Failed saving pollen record ({display_name} @ {dt}): {e}")

        try:
            print(f"COUNT OF POLLEN DATA: {PollenData.objects.count()}")
        except Exception:
            pass



    def fetch_pollen_forecast(self, location, days, pageSize, pageToken, languageCode, plantsDescription):
        """
        Fetch pollen forecast data from Google pollen API.
        """
        self.validate_pageSize(pageSize)
        self.validate_days(days)
        latitude, longitude = location['latitude'], location['longitude']

        url = f"{self.baseUrl}/forecast:lookup?key={API_KEY}"

        params = {
            "key": API_KEY,
            "location.latitude": latitude,
            "location.longitude": longitude,
            "days": days,
            "pageSize": pageSize,
            "pageToken": pageToken,
            "languageCode": languageCode,
            "plantsDescription": plantsDescription
        }
        params = {k: v for k, v in params.items() if v is not None}

        try:
            response = requests.get(url, params=params)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching pollen forecast data: {e}")
            return None
    
    def filter_pollen_data(self, pollen_data):
        """Filter pollen data to only include Grass, Tree, and Weed pollenTypeInfo"""
        if not pollen_data or 'dailyInfo' not in pollen_data:
            return pollen_data
            
        filtered_data = {
            'regionCode': pollen_data.get('regionCode'),
            'dailyInfo': []
        }
        
        for daily_info in pollen_data['dailyInfo']:
            filtered_daily = {
                'date': daily_info.get('date'),
                'pollenTypeInfo': []
            }
            
            # Filter only Grass, Tree, and Weed from pollenTypeInfo
            if 'pollenTypeInfo' in daily_info:
                for pollen_type in daily_info['pollenTypeInfo']:
                    if pollen_type.get('code') in ['GRASS', 'TREE', 'WEED']:
                        filtered_daily['pollenTypeInfo'].append(pollen_type)
            
            filtered_data['dailyInfo'].append(filtered_daily)
        
        return filtered_data


    def validate_pageSize(self, pageSize):
        """
        Validate pageSize to be within allowed limits.
        """
        if pageSize is None: return True
        if pageSize < 1 or pageSize > 5:
            raise ValueError("pageSize must be between 1 and 5")
        return True
    
    def validate_days(self, days):
        """
        Validate days to be within allowed limits.
        """
        if days is None: return True
        if days < 1 or days > 10:
            raise ValueError("days must be between 1 and 10")
        return True
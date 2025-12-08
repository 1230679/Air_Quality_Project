import time
import requests
import os
import logging
from .models import AirQualityData
from django.utils.dateparse import parse_datetime
from .config import API_KEY

logger = logging.getLogger('data_fetching.aqi')

class AirQualityIndex:
    def __init__(self):
        self.baseUrl = "https://airquality.googleapis.com/v1/"

    class ExtraComputations:
        EXTRA_COMPUTATION_UNSPECIFIED = "EXTRA_COMPUTATION_UNSPECIFIED"
        LOCAL_AQI = "LOCAL_AQI"
        HEALTH_RECOMMENDATIONS = "HEALTH_RECOMMENDATIONS"
        POLLUTANT_ADDITIONAL_INFO = "POLLUTANT_ADDITIONAL_INFO"
        DOMINANT_POLLUTANT_CONCENTRATION = "DOMINANT_POLLUTANT_CONCENTRATION"
        POLLUTANT_CONCENTRATION = "POLLUTANT_CONCENTRATION"

        def get_string(self, computations):
            return [e for e in computations]

    def fill_db(self, location, response):
        """
        Parse the Google Air Quality API response and store/update hourly data in the DB.
        """
        # change response from json to dict
        hours_info = response.get("hoursInfo", [])
        logger.info("fill_db called for aqi; location=%s; received %d hourly entries", getattr(location, 'id', location), len(hours_info))

        for hour_data in hours_info:
            logger.debug("Processing hour data: %s", hour_data)
            try:
                timestamp = parse_datetime(hour_data["dateTime"])
            except Exception:
                logger.exception("Failed parsing dateTime from hour_data: %s", hour_data)
                continue
            indexes = hour_data.get("indexes", [])
            uaqi_entry = next((i for i in indexes if i.get("code") == "uaqi"), None)

            aqi = uaqi_entry.get("aqi") if uaqi_entry else None
            dominant_pollutant = uaqi_entry.get("dominantPollutant") if uaqi_entry else None

            # Pollutant concentrations
            pollutants = {p["code"]: p for p in hour_data.get("pollutants", [])}

            pm25 = pollutants.get("pm25", {}).get("concentration", {}).get("value")
            pm10 = pollutants.get("pm10", {}).get("concentration", {}).get("value")
            o3 = pollutants.get("o3", {}).get("concentration", {}).get("value")
            no2 = pollutants.get("no2", {}).get("concentration", {}).get("value")

            try:
                AirQualityData.objects.update_or_create(
                    location_id=location,
                    timestamp=timestamp,
                    defaults={
                        "aqi": int(aqi) if aqi is not None else None,
                        "dominant_pollutant": dominant_pollutant or "",
                        "pm25_concentration": float(pm25) if pm25 is not None else None,
                        "pm10_concentration": float(pm10) if pm10 is not None else None,
                        "o3_concentration": float(o3) if o3 is not None else None,
                        "no2_concentration": float(no2) if no2 is not None else None,
                    },
                )
            except Exception:
                logger.exception("Failed saving AirQualityData for timestamp %s", timestamp)
        try:
            logger.info("AirQualityData count: %s", AirQualityData.objects.count())
        except Exception:
            logger.exception("Failed to count AirQualityData")

        

    def fetch_aqi_history(self, pageSize=None, pageToken=None, location=None, extraComputations=None, uaqiColorPalette=None, customLocalAqis=None, dateTime=None, hours=None, period=None, universalAqi=None, languageCode=None):
        self.validate_pageSize(pageSize) # checks if pageSize is valid
        # self.validate_extraComputations(extraComputations) # checks if extraComputations is valid
        latitude, longitude = location['latitude'], location['longitude'] # extract lat and long from location

        url = f"{self.baseUrl}history:lookup?key={API_KEY}"

        extraComputationsValues = self.ExtraComputations().get_string(extraComputations) if extraComputations else None
        body = {
            "pageSize": pageSize,
            "pageToken": pageToken,
            "location": {
                "latitude": latitude,
                "longitude": longitude
            },
            "extraComputations": extraComputationsValues,
            "uaqiColorPalette": uaqiColorPalette,
            "customLocalAqis": customLocalAqis,
            "dateTime": dateTime,
            "hours": hours,
            "period": period,
            "universalAqi": universalAqi,
            "languageCode": languageCode
        }
        
        body = {k: v for k, v in body.items() if v is not None}  # Remove None values
        try:
            response = requests.post(url, json=body)
            response.raise_for_status()
            return response.json()
        
        except requests.exceptions.RequestException as e:
            logger.exception("Error fetching AQI data: %s", e)
            return None

    def validate_pageSize(self, pageSize):
        if pageSize is None: return True
        if pageSize < 1 or pageSize > 168:
            raise ValueError("pageSize must be between 1 and 168")
        return True
    
    def validate_extraComputations(self, extraComputations):
        if extraComputations is None: return True
        is_valid = all([computation in AirQualityIndex.ExtraComputations().__dict__.values() for computation in extraComputations])
        if not is_valid:
            raise ValueError("One or more extraComputations are invalid")
        return True




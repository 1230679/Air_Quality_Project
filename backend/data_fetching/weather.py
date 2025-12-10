import os 
import requests
import logging
from .models import WeatherData
from django.utils.dateparse import parse_datetime
from .config import API_KEY

logger = logging.getLogger('data_fetching.weather')

class Weather:
    def __init__(self):
        self.baseUrl = "https://weather.googleapis.com/v1"

    def fill_db(self, location, response):
        history_hours = response.get("historyHours", [])
        logger.info("fill_db called for weather; location=%s; history_hours=%d", getattr(location, 'id', location), len(history_hours))

        for hour_data in history_hours:
            interval = hour_data.get("interval")
            raw_end = None
            if isinstance(interval, dict):
                raw_end = interval.get("endTime")
            else:
                raw_end = hour_data.get("endTime")

            end_time = None
            try:
                if raw_end is None:
                    end_time = None
                elif isinstance(raw_end, str):
                    end_time = parse_datetime(raw_end)
                elif isinstance(raw_end, (int, float)):
                    ts = float(raw_end)
                    if ts > 1e12:
                        ts = ts / 1000.0
                    from datetime import datetime, timezone
                    end_time = datetime.fromtimestamp(ts, tz=timezone.utc)
                elif isinstance(raw_end, dict):
                    if raw_end.get("dateTime"):
                        end_time = parse_datetime(raw_end.get("dateTime"))
                    elif raw_end.get("value") and isinstance(raw_end.get("value"), str):
                        end_time = parse_datetime(raw_end.get("value"))
                    elif raw_end.get("seconds") is not None:
                        s = raw_end.get("seconds")
                        s = float(s)
                        if s > 1e12:
                            s = s / 1000.0
                        from datetime import datetime, timezone
                        end_time = datetime.fromtimestamp(s, tz=timezone.utc)
                    else:
                        end_time = parse_datetime(str(raw_end))
            except Exception:
                end_time = None

            if end_time is None:
                # Skip records without a valid timestamp to avoid DB integrity issues
                continue

            temperature = hour_data.get("temperature", {}).get("degrees")
            apparent_temperature = hour_data.get("feelsLikeTemperature", {}).get("degrees")
            humidity = hour_data.get("humidity")
            wind_speed = hour_data.get("wind", {}).get("speed", {}).get("value")
            uv_index = hour_data.get("uvIndex")

            try:
                WeatherData.objects.update_or_create(
                location_id=location,
                timestamp=end_time,
                temperature = float(temperature) if temperature is not None else None,
                apparent_temperature = float(apparent_temperature) if apparent_temperature is not None else None,
                humidity = float(humidity) if humidity is not None else None,
                wind_speed = float(wind_speed) if wind_speed is not None else None,
                uv_index = int(uv_index) if uv_index is not None else None,
                )
            except Exception:
                logger.exception("Failed saving WeatherData for %s: %s", end_time, hour_data)

        try:
            logger.info("COUNT OF WEATHER DATA: %s", WeatherData.objects.count())
        except Exception:
            logger.exception("Failed to count WeatherData")

    def fetch_weather_data(self, location):
        """
        Fetch hourly historical weather data from Google Weather API.
        
        Args:
            location (dict): Dictionary with 'latitude' and 'longitude' keys.
            hours (int): Number of hours of history to fetch.
            pageSize (int, optional): Number of results per page. Defaults to 5.
        Returns:
            dict: Parsed JSON response containing historical weather data.
        """
        
        url = f"{self.baseUrl}/history/hours:lookup?key={API_KEY}"

        params = {
            "key": API_KEY,
            "location.latitude": location["latitude"],
            "location.longitude": location["longitude"]
        }

        logger.info("Fetching weather data from %s for location=%s", url, location)

        try:
            response = requests.get(url, params=params)
            response.raise_for_status()
            data = response.json()
            try:
                history_count = len(data.get("historyHours", [])) if isinstance(data, dict) else None
                logger.info("Fetched weather data; history_hours=%s", history_count)
            except Exception:
                logger.debug("Fetched weather data keys: %s", list(data.keys()) if isinstance(data, dict) else type(data))
            return data
        except requests.exceptions.RequestException as e:
            logger.exception("Error fetching weather data: %s", e)
            return None
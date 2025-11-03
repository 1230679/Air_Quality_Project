import os 
import requests
from .models import WeatherData
from django.utils.dateparse import parse_datetime

API_KEY = os.getenv("GOOGLE_API_KEY")  # You might want to rename this to GOOGLE_WEATHER_API_KEY

class Weather:
    def __init__(self):
        self.baseUrl = "https://weather.googleapis.com/v1/"

    def fill_db(self, location, response):
        history_hours = response.get("historyHours", [])

        for hour_data in history_hours:
            interval = hour_data.get("interval")
            end_time = parse_datetime(hour_data.get("endTime")) # Only retrieve end_time and use only end_time as timestamp to avoid conflicts
            temperature = hour_data.get("temperature", {}).get("degrees")
            apparent_temperature = hour_data.get("feelsLikeTemperature", {}).get("degrees")
            humidity = hour_data.get("humidity")
            wind_speed = hour_data.get("wind", {}).get("speed", {}).get("value")
            uv_index = hour_data.get("uvIndex")

            WeatherData.objects.update_or_create(
                location_id=location,
                timestamp=end_time,
                temperature = float(temperature) if temperature is not None else None,
                apparent_temperature = float(apparent_temperature) if apparent_temperature is not None else None,
                humidity = float(humidity) if humidity is not None else None,
                wind_speed = float(wind_speed) if wind_speed is not None else None,
                uv_index = int(uv_index) if uv_index is not None else None,
            )

        print(f"COUNT OF WEATHER DATA: {WeatherData.objects.count()}")

    def fetch_weather_data(self, location, hours, pageSize=5):
        """
        Fetch hourly historical weather data from Google Weather API.

        """
        
        url = f"{self.baseUrl}/history:lookup?key={API_KEY}"

        params = {
            "key": API_KEY,
            "location.latitude": location["latitude"],
            "location.longitude": location["longitude"],
            "hours": hours,
            "pageSize": pageSize
        }

        try:
            response = requests.get(url, params=params)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching weather data: {e}")
            return None
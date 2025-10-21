import requests
import os
from dotenv import load_dotenv

load_dotenv() # This loads variables from your .env.dev file
API_KEY = os.getenv("GOOGLE_API_KEY")  # You might want to rename this to GOOGLE_API_KEY

POLLEN_BASE_URL = "https://pollen.googleapis.com/"
AQ_BASE_URL = "https://airquality.googleapis.com/"

def fetch_pollen_data(latitude, longitude, days=1):
    url = "https://pollen.googleapis.com/v1/forecast:lookup"
    
    try:
        response = requests.get(url, params={
            'location.latitude': latitude,
            'location.longitude': longitude,
            'days': days,
            'key': API_KEY
        })
        
        response.raise_for_status()  # Raises an exception for bad status codes
        return response.json()
        
    except requests.exceptions.RequestException as e:
        print(f"Error fetching pollen data: {e}")
        return None
    
def fetch_air_quality_data(latitude, longitude, hours, page_size=5):
    """
    Retrieve historical hourly air quality data from the Google Air Quality API.

    Args:
        latitude (float): Latitude of the location.
        longitude (float): Longitude of the location.
        start_time (str): Start time in ISO 8601 format (e.g. "2023-06-15T08:00:00Z").
        end_time (str): End time in ISO 8601 format (e.g. "2023-06-15T12:00:00Z").
        page_size (int): Maximum number of records per page (default: 100).

    Returns:
        list: A list of hourly air quality records (each as a dict).
    """
    url = f"https://airquality.googleapis.com/v1/history:lookup?key={API_KEY}"

    body = {
        "hours": hours,
        "pageSize": page_size,
        "location": {
            "latitude": latitude,
            "longitude": longitude
        }
    }

    try:
        response = requests.post(url, json=body)
        response.raise_for_status()
        return response.json()
    
    except requests.exceptions.RequestException as e:
        print(f"Error fetching air quality data: {e}")
        return None

def fetch_weather_data(latitude, longitude, hours=12, start_time=None, end_time=None):
    """
    Retrieve hourly historical weather data from the Google Weather API.

    Args:
        latitude (float): Latitude of the location.
        longitude (float): Longitude of the location.
        hours (int, optional): Number of hours of history to fetch (default 12 if None).
        start_time (str, optional): ISO 8601 start timestamp, e.g. "2025-02-12T20:00:00Z".
        end_time (str, optional): ISO 8601 end timestamp, e.g. "2025-02-12T23:00:00Z".

    Returns:    
        dict: Parsed JSON response containing historical weather data.
    """
    url = "https://weather.googleapis.com/v1/history/hours:lookup"
    
    params = {
        "key": API_KEY,
        "location.latitude": latitude,
        "location.longitude": longitude
    }

    if hours is not None:
        params["hours"] = hours
    elif start_time and end_time:
        params["startTime"] = start_time
        params["endTime"] = end_time

    try:
        response = requests.get(url, params=params)
        response.raise_for_status()
        return response.json()
    
    except requests.exceptions.RequestException as e:
        print(f"Error fetching weather data: {e}")
        return None



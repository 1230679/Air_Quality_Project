import requests
import os
from dotenv import load_dotenv

load_dotenv() # This loads variables from your .env.dev file
API_KEY = os.getenv("GOOGLE_API_KEY")  # You might want to rename this to GOOGLE_API_KEY


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

from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from .fetchers import fetch_pollen_data, fetch_air_quality_data, get_hourly_weather_history, fetch_weather_data
# Create your views here.


class FetchPollenData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700

    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"
    
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
    
    def get(self, request, *args, **kwargs):
        pollen_data = fetch_pollen_data(latitude=self.latitude, longitude=self.longitude)
        
        if pollen_data:
            # Filter the data to only include Grass, Tree, Weed
            filtered_data = self.filter_pollen_data(pollen_data)
            pprint(filtered_data)
            return Response({"pollen": filtered_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class FetchAirQualityData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700


    def get(self, request, *args, **kwargs):
        aq_data = fetch_air_quality_data(latitude=self.latitude, longitude=self.longitude, start_time=None, end_time=None)

        pprint(aq_data)
        # pprint(weather_data)
        # if pollen_data and aq_data and weather_data:
        if aq_data:
            return Response({"air_quality": aq_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch air quality data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class FetchWeatherData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700

    # start_time = "2025-06-15T08:00:00Z"
    # end_time = "2025-06-15T12:00:00Z"

    def get(self, request, *args, **kwargs):
        weather_data = fetch_weather_data(
            latitude=self.latitude,
            longitude=self.longitude)

        pprint(weather_data)
        # pprint(weather_data)
        # if pollen_data and aq_data and weather_data:
        if weather_data:
            return Response({"weather": weather_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch weather data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
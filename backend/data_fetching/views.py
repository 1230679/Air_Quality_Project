from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from .fetchers import fetch_pollen_data, fetch_air_quality_data, get_hourly_weather_history
# Create your views here.


class FetchData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700
    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"
    def get(self, request, *args, **kwargs):
        pollen_data = fetch_pollen_data(latitude=self.latitude, longitude=self.longitude)
        aq_data = fetch_air_quality_data(latitude=self.latitude, longitude=self.longitude, start_time=self.start_time, end_time=self.end_time)
        weather_data = get_hourly_weather_history(latitude=self.latitude, longitude=self.longitude, start_time=self.start_time, end_time=self.end_time)

        pprint(pollen_data)
        pprint(aq_data)
        pprint(weather_data)
        if pollen_data and aq_data and weather_data:
            return Response({"pollen": pollen_data, "air_quality": aq_data, "weather": weather_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)



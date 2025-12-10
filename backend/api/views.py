from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from data_fetching.views import FetchPollenData, FetchAirQualityData, FetchWeatherData

class PollenData(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, *args, **kwargs):
        fetch_view = FetchPollenData()
        response = fetch_view.get(request, *args, **kwargs)
        print("Response from FetchPollenData view:")
        pprint(response.data)
        return response
    
class AirQualityData(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, *args, **kwargs):
        fetch_view = FetchAirQualityData()
        response = fetch_view.get(request, *args, **kwargs)
        print("Response from FetchAirQualityData view:")
        pprint(response.data)
        return response
    
    def get_history(self, request, *args, **kwargs):
        fetch_view = FetchAirQualityData()
        response = fetch_view.get_history(request, *args, **kwargs)
        print("Response from FetchAirQualityData get_history view:")
        pprint(response.data)
        return response

class WeatherData(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, *args, **kwargs):
        fetch_view = FetchWeatherData()
        response = fetch_view.get(request, *args, **kwargs)
        print("Response from FetchWeatherData view:")
        pprint(response.data)
        return response





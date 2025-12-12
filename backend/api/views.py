from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from data_fetching.views import FetchPollenData, FetchAirQualityData, FetchWeatherData
from airquality_ml.aqi_runtime import predict_next_24h_aqi_for_location

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

class AQIForecastData(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, *args, **kwargs):
        """
        GET /api/aqi_forecast/?lat=56.1656&lon=10.2123

        Returns 24-hour AQI forecast based on Google Weather API +
        the trained Prophet model.
        """
        try:
            lat = float(request.query_params.get("lat", "56.1656"))
            lon = float(request.query_params.get("lon", "10.2123"))

            forecast = predict_next_24h_aqi_for_location(lat, lon)

            # forecast is already a list[dict] with timestamp, aqi, aqi_lower, aqi_upper
            return Response(forecast, status=status.HTTP_200_OK)

        except Exception as e:
            # For debugging you can log e or use pprint if you want
            return Response(
                {"detail": str(e)},
                status=status.HTTP_500_INTERNAL_SERVER_ERROR,
            )



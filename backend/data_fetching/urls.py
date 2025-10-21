from django.urls import include, path

from .views import FetchPollenData, FetchAirQualityData, FetchWeatherData

urlpatterns = [
    path('fetch-data/', FetchPollenData.as_view(), name='fetch_data'),
    path('fetch-aiq-data/', FetchAirQualityData.as_view(), name='fetch_aiq_data'),
    path('fetch-weather-data/', FetchWeatherData.as_view(), name='fetch_weather_data')
]
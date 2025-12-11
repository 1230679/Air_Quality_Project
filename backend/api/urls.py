from django.urls import include, path

from .views import PollenData, AirQualityData, WeatherData

urlpatterns = [
    path('pollen_data/', PollenData.as_view(), name='fetch_data'),
    path('aiq_data/', AirQualityData.as_view(), name='fetch_aiq_data'),
    path('weather_data/', WeatherData.as_view(), name='fetch_weather_data'),
    path('history_aqi_data/', AirQualityData.as_view(), name='fetch_history_aqi_data')
]
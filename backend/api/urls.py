from django.urls import include, path

from .views import PollenData, WeatherData, AirQualityData

urlpatterns = [
    path('polen/', PollenData.as_view(), name='fetch_data'),
    path('weather/', WeatherData.as_view(), name='fetch_weather_data'),
    path('air_quality/', AirQualityData.as_view(), name='fetch_air_quality_data'),
]
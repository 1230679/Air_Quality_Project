from django.urls import include, path

from .views import FetchPollenData, FetchAirQualityData, FetchWeatherData, task_status, test_celery, trigger_air_quality_fetch

urlpatterns = [
    path('fetch-data/', FetchPollenData.as_view(), name='fetch_data'),
    path('fetch-aiq-data/', trigger_air_quality_fetch, name='trigger_air_quality_fetch'),
    path('fetch-weather-data/', FetchWeatherData.as_view(), name='fetch_weather_data'),
    path('test-celery/', test_celery, name='test_celery'),
    path('task-status/<str:task_id>/', task_status, name='task_status'),

]
from django.shortcuts import render
from data_fetching.aqi import AirQualityIndex
from data_fetching.location import Location 
from data_fetching.pollen import Pollen
from data_fetching.weather import Weather
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .tasks import fetch_air_quality_data, process_air_quality_data, test_celery_task
from celery.result import AsyncResult
from celery import chain


class FetchPollenData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700

    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"

    pollen = Pollen()
    location_helper = Location()  # helper instance only; do not touch DB at import time
    
    def get(self, request, *args, **kwargs):
        location_obj = self.location_helper.fill_db(location=f"{self.latitude},{self.longitude}", city="Aarhus", country="Denmark")
        pollen_data = self.pollen.fetch_pollen_forecast(
            location={"latitude": self.latitude, "longitude": self.longitude},
            days=3,
            pageSize=5,
            pageToken=None,
            languageCode="en",
            plantsDescription=False
        )

        if pollen_data:
            # Filter the data to only include Grass, Tree, Weed
            filtered_data = self.pollen.filter_pollen_data(pollen_data)
            pprint(filtered_data)
            self.pollen.fill_db(location=location_obj, response=filtered_data)
            return Response({"pollen": filtered_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
class FetchAirQualityData(APIView):
    renderer_classes = [JSONRenderer]

    aqi = AirQualityIndex()

    latitude = 56.157200
    longitude = 10.210700

    location_helper = Location()  # do not call fill_db here

    def get(self, request, *args, **kwargs):
        # Create or update Location row at request time (avoids DB access during import)
        location_obj = self.location_helper.fill_db(location=f"{self.latitude},{self.longitude}", city="Aarhus", country="Denmark")

        aq_data = self.aqi.fetch_aqi_history(
            location={"latitude": self.latitude, "longitude": self.longitude},
            hours=2,
            extraComputations=[self.aqi.ExtraComputations.POLLUTANT_CONCENTRATION]
        )
        if aq_data:
            pprint(aq_data)
            self.aqi.fill_db(location=location_obj, response=aq_data)
            return Response({"air_quality": aq_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch air quality data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    

class FetchWeatherData(APIView):
    renderer_classes = [JSONRenderer]

    latitude = 56.157200
    longitude = 10.210700

    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"

    weather = Weather()
    location_helper = Location()  # helper only

    def get(self, request, *args, **kwargs):
        # Create or update Location row at request time
        location_obj = self.location_helper.fill_db(location=f"{self.latitude},{self.longitude}", city="Aarhus", country="Denmark")

        weather_data = self.weather.fetch_weather_data(
            location={"latitude": self.latitude, "longitude": self.longitude}
        )
        if weather_data:
            pprint(weather_data)
            self.weather.fill_db(location=location_obj, response=weather_data)
            return Response({"weather": weather_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch weather data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@require_http_methods(["GET"])
def trigger_air_quality_fetch(request):
    """
    Trigger the celery workflow to fetch and process air quality data
    """
    workflow = chain(
        fetch_air_quality_data.s(),
        process_air_quality_data.s()
    )
    result = workflow.apply_async()

    return JsonResponse({
        "message": "Air quality fetch enqueued",
        "task_id": result.id,
        "status_url": f"/data-fetching/task-status/{result.id}/"
    })



@require_http_methods(["GET"])
def test_celery(request):
    """
    Test if Celery is working
    """
    task = test_celery_task.delay()
    
    return JsonResponse({
        'message': 'Celery test task triggered!',
        'task_id': task.id,
        'status': 'Task is running...',
        'check_status_at': f'http://localhost:8000/data-fetching/task-status/{task.id}/'
    })

@require_http_methods(["GET"])
def task_status(request, task_id):
    """
    Check the status of a Celery task
    """
    from celery.result import AsyncResult
    
    task_result = AsyncResult(task_id)
    
    response = {
        'task_id': task_id,
        'status': task_result.status,
        'ready': task_result.ready()
    }
    
    if task_result.ready():
        if task_result.successful():
            response['result'] = task_result.result
        else:
            response['error'] = str(task_result.info)
    
    return JsonResponse(response)




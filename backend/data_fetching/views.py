from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from .fetchers import fetch_pollen_data, fetch_air_quality_data, fetch_weather_data
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from .tasks import fetch_air_quality_data, process_air_quality_data, test_celery_task
from celery.result import AsyncResult

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
        # aq_data = fetch_air_quality_data(latitude=self.latitude, longitude=self.longitude, start_time=self.start_time, end_time=self.end_time)
        # weather_data = get_hourly_weather_history(latitude=self.latitude, longitude=self.longitude, start_time=self.start_time, end_time=self.end_time)

        
        # pprint(aq_data)
        # pprint(weather_data)
        # if pollen_data and aq_data and weather_data:
        if pollen_data:
            # Filter the data to only include Grass, Tree, Weed
            filtered_data = self.filter_pollen_data(pollen_data)
            pprint(filtered_data)
            return Response({"pollen": filtered_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
class FetchAirQualityData(APIView):
    renderer_classes = [JSONRenderer]

    latitude = 56.157200
    longitude = 10.210700

    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"

    def get(self, request, *args, **kwargs):
        aq_data = fetch_air_quality_data(latitude=self.latitude, longitude=self.longitude, hours=240)
        if aq_data:
            pprint(aq_data)
            return Response({"air_quality": aq_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch air quality data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    

class FetchWeatherData(APIView):
    renderer_classes = [JSONRenderer]

    latitude = 56.157200
    longitude = 10.210700

    start_time = "2025-06-15T08:00:00Z"
    end_time = "2025-06-15T12:00:00Z"

    def get(self, request, *args, **kwargs):
        weather_data = fetch_weather_data(latitude=self.latitude, longitude=self.longitude, hours=10)
        if weather_data:
            pprint(weather_data)
            return Response({"weather": weather_data}, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch weather data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


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




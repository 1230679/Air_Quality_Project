from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from .fetchers import fetch_pollen_data
# Create your views here.


class FetchPollenData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700
    
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
            return Response(filtered_data, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch pollen data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)



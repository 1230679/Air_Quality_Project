from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from .fetchers import fetch_pollen_data
# Create your views here.


class FetchData(APIView):
    # Force JSON-only responses
    renderer_classes = [JSONRenderer]

    # Default coordinates for Aarhus, Denmark
    latitude = 56.157200
    longitude = 10.210700
    def get(self, request, *args, **kwargs):
        pollen_data = fetch_pollen_data(latitude=self.latitude, longitude=self.longitude)
        pprint(pollen_data)
        if pollen_data:
            return Response(pollen_data, status=status.HTTP_200_OK)
        return Response({"error": "Failed to fetch pollen data"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)



from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer
from pprint import pprint
from data_fetching.views import FetchPollenData

class PollenData(APIView):
    renderer_classes = [JSONRenderer]


    # get pollen data from FetchPollenData view
    def get(self, request, *args, **kwargs):
        fetch_view = FetchPollenData()
        response = fetch_view.get(request, *args, **kwargs)
        print("Response from FetchPollenData view:")
        pprint(response.data)
        return response



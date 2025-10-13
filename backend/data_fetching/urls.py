from django.urls import include, path

from .views import FetchPollenData

urlpatterns = [
    path('fetch-data/', FetchPollenData.as_view(), name='fetch_data'),
]
from django.urls import include, path

from .views import FetchData

urlpatterns = [
    path('fetch-data/', FetchData.as_view(), name='fetch_data'),
]
from django.urls import include, path

from .views import PollenData

urlpatterns = [
    path('polen_data/', PollenData.as_view(), name='fetch_data'),
]
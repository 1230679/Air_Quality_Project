from celery import shared_task
from data_fetching.aqi import AirQualityIndex
from data_fetching.weather import Weather
from data_fetching.location import Location
import logging


logger = logging.getLogger(__name__)

@shared_task
def fetch_weather_data():
    logger.info("Fetching weather data...")
    weather = Weather()
    latitude = 56.157200
    longitude = 10.210700

    weather_data = weather.fetch_weather_data(
        location={"latitude": latitude, "longitude": longitude}
    )

    return weather_data 

@shared_task
def process_weather_data(data):
    logger.info("Processing weather data...")
    weather = Weather()
    location_helper = Location()
    location_obj = location_helper.fill_db(location="56.157200,10.210700", city="Aarhus", country="Denmark")
    weather.fill_db(location=location_obj, response=data)

    return "Weather data processed successfully"

@shared_task
def fetch_air_quality_data():
    """
    Fetch air quality data from external API
    """
    logger.info("Starting air quality data fetch...")
    aqi = AirQualityIndex()
    latitude = 56.157200
    longitude = 10.210700

    aq_data = aqi.fetch_aqi_history(
        location={"latitude": latitude, "longitude": longitude},
        hours=2,
        extraComputations=[aqi.ExtraComputations.POLLUTANT_CONCENTRATION]
    )

    return aq_data

@shared_task
def process_air_quality_data(data):
    """
    Process fetched air quality data
    """
    logger.info("Processing air quality data...")
    aqi = AirQualityIndex()
    location_helper = Location()
    location_obj = location_helper.fill_db(location="56.157200,10.210700", city="Aarhus", country="Denmark")
    aqi.fill_db(location=location_obj, response=data)

    return "Data processed successfully"

@shared_task
def test_celery_task():
    """
    Simple test task to verify Celery is working
    """
    import time
    import logging
    
    logger = logging.getLogger(__name__)
    logger.info("🚀 Test task started!")
    
    time.sleep(3)
    
    logger.info("✅ Test task completed successfully!")
    return "Test task executed successfully at " + str(time.time())
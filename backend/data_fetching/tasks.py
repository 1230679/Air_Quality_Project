from celery import shared_task
import logging

logger = logging.getLogger(__name__)

@shared_task
def fetch_air_quality_data():
    """
    Fetch air quality data from external API
    """
    logger.info("Starting air quality data fetch...")
    # Add your data fetching logic here
    return "Data fetched successfully"

@shared_task
def process_air_quality_data(data):
    """
    Process fetched air quality data
    """
    logger.info("Processing air quality data...")
    # Add your data processing logic here
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
    
    time.sleep(3)  # Simulate some work
    
    logger.info("✅ Test task completed successfully!")
    return "Test task executed successfully at " + str(time.time())
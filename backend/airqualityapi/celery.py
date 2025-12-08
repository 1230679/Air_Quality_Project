import os
from celery import Celery
from celery.schedules import crontab

# Set the default Django settings module for the 'celery' program.
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'airqualityapi.settings')

app = Celery('airqualityapi')

# Using a string here means the worker doesn't have to serialize
# the configuration object to child processes.
# - namespace='CELERY' means all celery-related configuration keys
#   should have a `CELERY_` prefix.
app.config_from_object('django.conf:settings', namespace='CELERY')

# Load task modules from all registered Django apps.
app.autodiscover_tasks()

# Periodic task schedule
app.conf.beat_schedule = {
    'fetch-air-quality-every-4-hours': {
        'task': 'data_fetching.tasks.fetch_air_quality_data',
        'schedule': crontab(minute=0, hour='*/2'),  # Every 2 hours at minute 0
        # 'schedule': crontab(minute='*/2'),  # Every 2 minutes for testing
    },
}

app.conf.timezone = 'UTC'

@app.task(bind=True, ignore_result=True)
def debug_task(self):
    print(f'Request: {self.request!r}')

from .models import Location as LocationModel
import logging

logger = logging.getLogger('data_fetching.location')

class Location:
    def __init__(self):
        pass

    def fill_db(self, location, city, country):
       logger.info("fill_db called for Location; coords=%s city=%s country=%s", location, city, country)
       location, created = LocationModel.objects.update_or_create(
            coordinates=location,
            city=city,
            country=country,
        )
       if created:
           logger.info("Created new Location id=%s", location.id)
       return location
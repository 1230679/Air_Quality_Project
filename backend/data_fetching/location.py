from .models import Location as LocationModel

class Location:
    def __init__(self):
        pass

    def fill_db(self, location, city, country):
       location, created = LocationModel.objects.update_or_create(
            coordinates=location,
            city=city,
            country=country,
        )
       return location
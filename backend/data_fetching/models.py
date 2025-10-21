from django.db import models
from django.contrib.auth.models import User


class Location(models.Model):
    id = models.AutoField(primary_key=True)
    coordinates = models.CharField(max_length=255)
    city = models.CharField(max_length=255)
    country = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.city}, {self.country}"


class UserFavoriteLocation(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="favorite_locations")
    location = models.ForeignKey(Location, on_delete=models.CASCADE, related_name="favorited_by")

    class Meta:
        unique_together = ('user', 'location')

    def __str__(self):
        return f"{self.user.username} → {self.location.city}"


class AirQualityData(models.Model):
    id = models.AutoField(primary_key=True)
    location_id = models.ForeignKey(Location, on_delete=models.CASCADE, related_name="air_quality_data")
    timestamp = models.DateTimeField()
    aqi = models.IntegerField()
    dominant_pollutant = models.CharField(max_length=50)
    pm25_concentration = models.FloatField(null=True, blank=True)
    pm10_concentration = models.FloatField(null=True, blank=True)
    o3_concentration = models.FloatField(null=True, blank=True)
    no2_concentration = models.FloatField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp']

    def __str__(self):
        return f"Air Quality ({self.location_id.city}) @ {self.timestamp}"


class WeatherData(models.Model):
    id = models.AutoField(primary_key=True)
    location_id = models.ForeignKey(Location, on_delete=models.CASCADE, related_name="weather_data")
    timestamp = models.DateTimeField()
    temperature = models.FloatField(null=True, blank=True)
    apparent_temperature = models.FloatField(null=True, blank=True)
    humidity = models.FloatField(null=True, blank=True)
    wind_speed = models.FloatField(null=True, blank=True)
    weather_code = models.IntegerField(null=True, blank=True)
    uv_index = models.IntegerField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp']

    def __str__(self):
        return f"Weather ({self.location_id.city}) @ {self.timestamp}"


class PollenData(models.Model):
    id = models.AutoField(primary_key=True)
    location = models.ForeignKey(Location, on_delete=models.CASCADE, related_name="pollen_data")
    timestamp = models.DateTimeField()
    pollen_type = models.CharField(max_length=50)
    index_value = models.FloatField()
    index_display_name = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp']

    def __str__(self):
        return f"{self.pollen_type} ({self.location.city}) @ {self.timestamp}"


class MLRecommendation(models.Model):
    id = models.AutoField(primary_key=True)
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="ml_recommendations")
    location = models.ForeignKey(Location, on_delete=models.CASCADE, related_name="ml_recommendations")
    recommendation_id = models.IntegerField()
    trigger_reason = models.CharField(max_length=255)
    generated_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Recommendation {self.recommendation_id} for {self.user.username}"


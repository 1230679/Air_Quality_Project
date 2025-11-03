import crontab

def fill_db(latitude, longitude):
    from data_fetching import fetchers
    from data_fetching import models
    from django.utils import timezone

    # Example coordinates for fetching data
    coords = [(latitude, longitude)]
    # loop over a time period to fetch historical data
    end_time = timezone.now()
    start_time = end_time - timezone.timedelta(hours=12)
    time_interval = timezone.timedelta(hours=1)

    # loop over start_time to end_time in 12-hour increments
    for current_time in range(0, 12):
        for lat, lon in coords:
            # Fetch air quality data
            air_quality_data = fetchers.fetch_air_quality_data(
                latitude=lat,
                longitude=lon,
                hours=12
            )

            if air_quality_data:
                location, created = models.Location.objects.get_or_create(
                    coordinates=f"{lat},{lon}",
                )

                for entry in air_quality_data.get("data", []):
                    models.AirQualityData.objects.update_or_create(
                        location_id=location,
                        timestamp=entry["timestamp"],
                        defaults={
                            "aqi": entry["aqi"],
                            "dominant_pollutant": entry["dominant_pollutant"],
                            "pm25_concentration": entry.get("pm25_concentration"),
                            "pm10_concentration": entry.get("pm10_concentration"),
                            "o3_concentration": entry.get("o3_concentration"),
                            "no2_concentration": entry.get("no2_concentration"),
                        }
                    )

            weather_data = fetchers.fetch_weather_data(
                latitude=lat,
                longitude=lon,
                hours=12
            )

            if weather_data:
                location, created = models.Location.objects.get_or_create(
                    coordinates=f"{lat},{lon}"
                )

                for entry in weather_data.get("data", []):
                    models.WeatherData.objects.update_or_create(
                        location_id=location,
                        timestamp=entry["timestamp"],
                        defaults={
                            "temperature": entry.get("temperature"),
                            "apparent_temperature": entry.get("apparent_temperature"),
                            "humidity": entry.get("humidity"),
                            "wind_speed": entry.get("wind_speed"),
                            "weather_code": entry.get("weather_code"),
                            "uv_index": entry.get("uv_index"),
                        }
                    )
    
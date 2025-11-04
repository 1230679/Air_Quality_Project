# Air Quality API Documentation

## API Endpoints & Response Examples

### Base URLs

- **API Base**: `http://localhost:8000/api/`
- **Data Fetching Base**: `http://localhost:8000/data-fetching/`

---

## 1. Pollen Data

**Endpoint**: `GET /api/pollen_data/`

**Description**: Fetches pollen forecast data for grass, tree, and weed pollen types.

**Request Example**:
```bash
curl http://localhost:8000/api/pollen_data/
```

**Response Example**:
```json
{
  "pollen": {
    "regionCode": "DK",
    "dailyInfo": [
      {
        "date": {
          "year": 2025,
          "month": 2,
          "day": 12
        },
        "pollenTypeInfo": [
          {
            "code": "GRASS",
            "displayName": "Grass",
            "indexInfo": {
              "value": 2,
              "category": "Moderate",
              "indexDescription": "Moderate pollen levels"
            }
          },
          {
            "code": "TREE",
            "displayName": "Tree",
            "indexInfo": {
              "value": 1,
              "category": "Low",
              "indexDescription": "Low pollen levels"
            }
          },
          {
            "code": "WEED",
            "displayName": "Weed",
            "indexInfo": {
              "value": 0,
              "category": "None",
              "indexDescription": "No pollen"
            }
          }
        ]
      }
    ]
  }
}
```

---

## 2. Air Quality Data

**Endpoint**: `GET /api/aiq_data/`

**Description**: Retrieves hourly air quality data including AQI and pollutant concentrations.

**Request Example**:
```bash
curl http://localhost:8000/api/aiq_data/
```

**Response Example**:
```json
{
  "air_quality": {
    "hoursInfo": [
      {
        "dateTime": "2025-02-12T10:00:00Z",
        "indexes": [
          {
            "code": "uaqi",
            "displayName": "Universal AQI",
            "aqi": 45,
            "aqiDisplay": "45",
            "color": {
              "red": 0.18,
              "green": 0.75,
              "blue": 0.35
            },
            "category": "Good air quality",
            "dominantPollutant": "pm25"
          }
        ],
        "pollutants": [
          {
            "code": "pm25",
            "displayName": "PM2.5",
            "fullName": "Fine particulate matter",
            "concentration": {
              "value": 12.5,
              "units": "MICROGRAMS_PER_CUBIC_METER"
            }
          },
          {
            "code": "pm10",
            "displayName": "PM10",
            "fullName": "Coarse particulate matter",
            "concentration": {
              "value": 20.3,
              "units": "MICROGRAMS_PER_CUBIC_METER"
            }
          },
          {
            "code": "o3",
            "displayName": "O3",
            "fullName": "Ozone",
            "concentration": {
              "value": 35.7,
              "units": "MICROGRAMS_PER_CUBIC_METER"
            }
          },
          {
            "code": "no2",
            "displayName": "NO2",
            "fullName": "Nitrogen dioxide",
            "concentration": {
              "value": 15.2,
              "units": "MICROGRAMS_PER_CUBIC_METER"
            }
          }
        ]
      }
    ]
  }
}
```

---

## 3. Weather Data

**Endpoint**: `GET /api/weather_data/`

**Description**: Fetches hourly weather data including temperature, humidity, and wind conditions.

**Request Example**:
```bash
curl http://localhost:8000/api/weather_data/
```

**Response Example**:
```json
{
  "weather": {
    "hourly": {
      "time": [
        "2025-02-12T10:00",
        "2025-02-12T11:00",
        "2025-02-12T12:00"
      ],
      "temperature_2m": [15.2, 16.5, 17.8],
      "apparent_temperature": [13.5, 14.8, 16.1],
      "relative_humidity_2m": [65, 62, 58],
      "wind_speed_10m": [12.3, 13.5, 14.2],
      "weather_code": [1, 1, 2],
      "uv_index": [3, 4, 5]
    },
    "hourly_units": {
      "time": "iso8601",
      "temperature_2m": "°C",
      "apparent_temperature": "°C",
      "relative_humidity_2m": "%",
      "wind_speed_10m": "km/h",
      "weather_code": "wmo code",
      "uv_index": ""
    }
  }
}
```

---

## Database Schema

### Available Tables and Fields

All data can be accessed directly from the PostgreSQL database or through Django ORM.

---

### 1. Location Table

**Table Name**: `data_fetching_location`

Stores geographic location information.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `coordinates` | String | Latitude/longitude (e.g., "56.1572,10.2107") |
| `city` | String | City name (e.g., "Aarhus") |
| `country` | String | Country name (e.g., "Denmark") |
| `created_at` | DateTime | Timestamp of record creation |

**Example Query**:
```python
from data_fetching.models import Location

locations = Location.objects.all()
aarhus = Location.objects.get(city="Aarhus")
```

---

### 2. AirQualityData Table

**Table Name**: `data_fetching_airqualitydata`

Stores air quality measurements and pollutant concentrations.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `location_id` | ForeignKey | Reference to Location table |
| `timestamp` | DateTime | Time of measurement |
| `aqi` | Integer | Universal Air Quality Index (0-500) |
| `dominant_pollutant` | String | Main pollutant (e.g., "pm25", "o3") |
| `pm25_concentration` | Float | PM2.5 in µg/m³ |
| `pm10_concentration` | Float | PM10 in µg/m³ |
| `o3_concentration` | Float | Ozone in µg/m³ |
| `no2_concentration` | Float | Nitrogen dioxide in µg/m³ |
| `so2_concentration` | Float | Sulfur dioxide in µg/m³ |
| `co_concentration` | Float | Carbon monoxide in µg/m³ |

**Example Query**:
```python
from data_fetching.models import AirQualityData

# Get latest 24 hours of data
recent_aqi = AirQualityData.objects.filter(
    location__city="Aarhus"
).order_by('-timestamp')[:24]

# Get data for specific time range
from django.utils import timezone
from datetime import timedelta

last_week = timezone.now() - timedelta(days=7)
aqi_data = AirQualityData.objects.filter(
    location__city="Aarhus",
    timestamp__gte=last_week
)
```

---

### 3. WeatherData Table

**Table Name**: `data_fetching_weatherdata`

Stores weather measurements.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `location_id` | ForeignKey | Reference to Location table |
| `timestamp` | DateTime | Time of measurement |
| `temperature` | Float | Temperature in °C |
| `apparent_temperature` | Float | Feels-like temperature in °C |
| `humidity` | Float | Relative humidity (%) |
| `wind_speed` | Float | Wind speed in km/h |
| `weather_code` | Integer | WMO weather code |
| `uv_index` | Integer | UV index (0-11+) |

**Weather Codes**:
- `0`: Clear sky
- `1-3`: Partly cloudy
- `45-48`: Fog
- `51-67`: Rain
- `71-77`: Snow
- `80-82`: Rain showers
- `95-99`: Thunderstorm

**Example Query**:
```python
from data_fetching.models import WeatherData

# Get current weather
current_weather = WeatherData.objects.filter(
    location__city="Aarhus"
).order_by('-timestamp').first()

# Get average temperature for last 24 hours
from django.db.models import Avg

avg_temp = WeatherData.objects.filter(
    location__city="Aarhus",
    timestamp__gte=timezone.now() - timedelta(hours=24)
).aggregate(Avg('temperature'))
```

---

### 4. PollenData Table

**Table Name**: `data_fetching_pollendata`

Stores pollen forecast information.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `location_id` | ForeignKey | Reference to Location table |
| `timestamp` | DateTime | Forecast date |
| `pollen_type` | String | Type: "GRASS", "TREE", or "WEED" |
| `index_value` | Float | Pollen index (0-5) |
| `index_display_name` | String | Category: "None", "Low", "Moderate", "High", "Very High" |

**Example Query**:
```python
from data_fetching.models import PollenData

# Get today's pollen forecast
today_pollen = PollenData.objects.filter(
    location__city="Aarhus",
    timestamp__date=timezone.now().date()
)

# Get grass pollen only
grass_pollen = PollenData.objects.filter(
    location__city="Aarhus",
    pollen_type="GRASS"
).order_by('-timestamp')[:7]  # Last 7 days
```

---

### 5. UserFavoriteLocation Table

**Table Name**: `data_fetching_userfavoritelocation`

Links users to their favorite locations.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `user_id` | ForeignKey | Reference to Django User table |
| `location_id` | ForeignKey | Reference to Location table |

**Unique Constraint**: `(user_id, location_id)`

**Example Query**:
```python
from data_fetching.models import UserFavoriteLocation

# Get all favorite locations for a user
favorites = UserFavoriteLocation.objects.filter(user=user)

# Add favorite location
UserFavoriteLocation.objects.create(user=user, location=location)
```

---

### 6. MLRecommendation Table

**Table Name**: `data_fetching_mlrecommendation`

Stores ML-generated recommendations for users.

| Field | Type | Description |
|-------|------|-------------|
| `id` | Integer (PK) | Auto-increment primary key |
| `user_id` | ForeignKey | Reference to Django User table |
| `location_id` | ForeignKey | Reference to Location table |
| `recommendation_id` | Integer | Recommendation type identifier |
| `trigger_reason` | Text | Explanation for recommendation |
| `generated_at` | DateTime | When recommendation was created |

**Example Query**:
```python
from data_fetching.models import MLRecommendation

# Get user's latest recommendations
recommendations = MLRecommendation.objects.filter(
    user=user
).order_by('-generated_at')[:10]

# Create new recommendation
MLRecommendation.objects.create(
    user=user,
    location=location,
    recommendation_id=1,
    trigger_reason="High pollen levels detected (index: 4)"
)
```
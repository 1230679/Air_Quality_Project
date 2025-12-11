# AirQuality API

## Overview

The REST API is a Django based service that pulls environmental data (airquality, weather, pollen) from Google's public APIs, stores it in a PostgreSQL database and exposes a thin REST layer for data fetching and data retrieval.

Key goals:
- Provide API endpoints to retrieve airquality, pollen and weather data.
- Provide API endpoints to initiate cronjobs to retrieve data from external providers
- Persistent data storage

## Quick Start

```bash
# 1. Clone the repository
git clone git@github.com:1230679/Air_Quality_Project.git

# 2. Move into backend directory
cd Air_Quality_Project/backend

# 3. Build and run docker containers
sudo docker compose -f docker-compose.yaml up -d --build
```

Open `http://localhost:8000/` and there is the API root.

## Configuration

Create a `.env.dev` file for the configuration

```env
POSTGRES_NAME=POSTGRES_NAME
POSTGRES_USER=POSTGRES_USER
POSTGRES_PASSWORD=POSTGRES_PASSWORD
SECRET_KEY=SECRET_KEY
DEBUG=DEBUG

# Breezometer API Key
GOOGLE_API_KEY=GOOGLE_API_KEY

# Docker Database Configuration
DOCKER_DB_HOST=DOCKER_DB_HOST
DOCKER_DB_PORT=DOCKER_DB_PORT

# Celery Configuration
CELERY_BROKER_URL=CELERY_BROKER_URL
CELERY_RESULT_BACKEND=CELERY_RESULT_BACKEND
```

| Setting               | Purpose                                                    | Example                                 |
|-----------------------|------------------------------------------------------------|------------------------------------------|
| GOOGLE_API_KEY        | Auth token for Google Weather, Air Quality, and Pollen APIs| `AIzaSy...`                              |
| SECRET_KEY     | Django cryptographic secret                                | `s3cr3t!`                               |
| DATABASE_URL          | SQLAlchemy‑style DB connection string (PostgreSQL recommended) | `db-dev` |
| CELERY_BROKER_URL     | Message broker for async tasks (Redis default)             | `redis://redis:6379/0`                  |
| CELERY_RESULT_BACKEND | Where task results are stored (same as broker works)       | `redis://redis:6379/0`                  |


## REST API 

The public API provides two seperate API subdirectory.

### API Endpoints - `api/`

The `api/` endpoints allow to retrieve current environmental data.

| URL                | View                 | Description                                                                                                           |
|--------------------|----------------------|-----------------------------------------------------------------------------------------------------------------------|
| api/pollen_data/   | fetch_data           | Calls the `PollenData` APIView – retrieves the latest pollen forecast, stores it in the DB, and returns the JSON.      |
| api/aiq_data/      | fetch_aiq_data       | Calls the `AirQualityData` APIView – fetches current air‑quality (AQI) data, saves it, and returns the JSON payload.   |
| api/weather_data/  | fetch_weather_data   | Calls the `WeatherData` APIView – obtains the latest weather information, persists it, and returns the JSON response.   |
| api/history_aqi_data/ | fetch_history_aqi_data | Calls the `AirQualityData` view’s `get_history` method – retrieves historical AQI data, stores it, and returns the JSON. |

### Endpoints - `data-fetching/`

The `data-fetching` initates the celery tasks for initializing automated data retrieval.

| URL                                          | View                     | Description                                                                                                                |
|----------------------------------------------|--------------------------|----------------------------------------------------------------------------------------------------------------------------|
| data-fetching/fetch-data/                    | fetch_data               | Triggers `FetchPollenData` – gets pollen forecast, filters to grass/tree/weed, saves to DB, returns JSON payload.          |
| data-fetching/fetch-weather-data/            | trigger_weather_fetch    | Starts the Celery workflow `fetch_weather_data → process_weather_data`; returns a task ID and a URL to check its status. |
| data-fetching/fetch-aiq-data/                | trigger_air_quality_fetch| Starts the Celery workflow `fetch_air_quality_data → process_air_quality_data`; returns a task ID and a status‑check URL. |
| data-fetching/fetch-weather-data/            | fetch_weather_data       | Directly calls `FetchWeatherData` view to pull current weather history, store it, and return the JSON response.            |
| data-fetching/test-celery/                   | test_celery              | Fires a simple Celery test task (sleep + log) to verify that the worker is alive; returns task ID and status URL.          |
| data-fetching/task-status/<str:task_id>/     | task_status              | Polls the given Celery task ID, reporting its `status`, whether it’s `ready`, and the result or error once completed.      |

## Cronjobs

| Task                     | Purpose                                                               | Flow (sequential execution)                                   |
|--------------------------|-----------------------------------------------------------------------|---------------------------------------------------------------|
| `fetch_weather_data`     | Calls `Weather.fetch_weather_data` for latitude/longitude (Aarhus) and returns the raw JSON response. | `fetch_weather_data` → (output)                               |
| `process_weather_data`   | Persists the JSON produced by `fetch_weather_data`. It creates/updates a `Location` record and then calls `Weather.fill_db`. | `process_weather_data` (expects input from `fetch_weather_data`) |
| `fetch_air_quality_data`| Calls `AirQualityIndex.fetch_aqi_history` (2‑hour window) and returns the raw JSON response. | `fetch_air_quality_data` → (output)                           |
| `process_air_quality_data`| Persists the AQI JSON returned by `fetch_air_quality_data`. It creates/updates a `Location` record and then calls `AirQualityIndex.fill_db`. | `process_air_quality_data` (expects input from `fetch_air_quality_data`) |
| `test_celery_task`      | Simple sanity‑check task that sleeps for a few seconds, logs progress, and returns a string containing the current timestamp. | Stand‑alone (no dependent flow)                               |

## References

| Service                     | Documentation URL                                            |
|-----------------------------|-------------------------------------------------------------|
| Google Air Quality API      | https://developers.google.com/maps/documentation/air-quality/reference                   |
| Google Weather API          | https://developers.google.com/maps/documentation/weather/overview                      |
| Google Pollen API           | https://developers.google.com/maps/documentation/pollen/reference/rest                        |
| Django REST Framework       | https://www.django-rest-framework.org/                     |
| Celery                      | https://docs.celeryproject.org/en/stable/                  |
| PostgreSQL                  | https://www.postgresql.org/docs/                            |

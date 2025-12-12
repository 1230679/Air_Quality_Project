# backend/airquality_ml/aqi_runtime.py

import os
import json
import requests
import numpy as np
import pandas as pd
from pathlib import Path
from prophet.serialize import model_from_json

# -----------------------------
# Load trained Prophet model
# -----------------------------

THIS_DIR = Path(__file__).resolve().parent
MODEL_PATH = THIS_DIR / "aqi_prophet_model.json"

with MODEL_PATH.open("r") as f:
    _model_json = json.load(f)

AQI_MODEL = model_from_json(_model_json)

REGRESSORS = [
    "temperature_2m",
    "relative_humidity_2m",
    "precipitation",
    "wind_speed_10m",
    "wind_gusts_10m",
    "wind_dir_sin",
    "wind_dir_cos",
]

GOOGLE_WEATHER_API_KEY = os.getenv("GOOGLE_API_KEY")


# -----------------------------
# 1. Call Google Weather API
# -----------------------------

def fetch_google_weather_24h(lat: float, lon: float):
    if not GOOGLE_WEATHER_API_KEY:
        raise RuntimeError("GOOGLE_WEATHER_API_KEY env var is not set")

    # Correct endpoint from Google docs
    url = "https://weather.googleapis.com/v1/forecast/hours:lookup"

    params = {
        "key": GOOGLE_WEATHER_API_KEY,
        "location.latitude": lat,
        "location.longitude": lon,
        "hours": 24,
        # optional:
        # "pageSize": 24,
    }

    r = requests.get(url, params=params, timeout=10)
    r.raise_for_status()
    data = r.json()

    # Correct top-level field
    hours = data.get("forecastHours", [])
    if len(hours) < 24:
        raise RuntimeError(f"Expected at least 24 forecastHours from Google Weather, got {len(hours)}")

    return hours[:24]


# -----------------------------
# 2. Convert Google `hours` -> DF
# -----------------------------

def google_hours_to_weather_df(hours):
    rows = []
    for h in hours[:24]:
        # Interval startTime is the hour timestamp
        ts = pd.to_datetime(h["interval"]["startTime"])
        if ts.tzinfo is not None:
            ts = ts.tz_convert(None)

        temperature = h["temperature"]["degrees"]
        humidity = h["relativeHumidity"]

        precip_obj = h.get("precipitation", {}).get("qpf", {})
        precip = precip_obj.get("quantity", 0.0)

        wind_obj = h["wind"]
        wind_dir = wind_obj["direction"]["degrees"]
        wind_speed = wind_obj["speed"]["value"]
        wind_gust = wind_obj.get("gust", {}).get("value", wind_speed)

        rows.append({
            "date": ts,
            "temperature_2m": temperature,
            "relative_humidity_2m": humidity,
            "precipitation": precip,
            "wind_speed_10m": wind_speed,
            "wind_gusts_10m": wind_gust,
            "wind_direction_10m": wind_dir,
        })

    df = pd.DataFrame(rows)
    df["wind_dir_sin"] = np.sin(np.deg2rad(df["wind_direction_10m"]))
    df["wind_dir_cos"] = np.cos(np.deg2rad(df["wind_direction_10m"]))
    return df



# -----------------------------
# 3. High-level prediction API
# -----------------------------

def predict_next_24h_aqi_for_location(lat: float, lon: float):
    """
    Main function your Django API will call.

    - fetches 24h weather from Google
    - converts to regressors
    - runs Prophet
    - returns list[dict] with AQI forecast
    """

    hours = fetch_google_weather_24h(lat, lon)
    df_weather = google_hours_to_weather_df(hours)

    df_future = df_weather.rename(columns={"date": "ds"})
    df_future["ds"] = pd.to_datetime(df_future["ds"])
    df_future = df_future[["ds"] + REGRESSORS]

    forecast = AQI_MODEL.predict(df_future)

    results = []
    for row in forecast.itertuples():
        results.append({
            "timestamp": row.ds.isoformat(),
            "aqi": float(row.yhat),
            "aqi_lower": float(row.yhat_lower),
            "aqi_upper": float(row.yhat_upper),
        })

    return results

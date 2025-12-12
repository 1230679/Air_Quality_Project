package com.example.livelifebreatheair.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AirQualityIndexForecastResponse (
    val entries: List<AirQualityIndexForecast>
)

@Serializable
data class AirQualityIndexForecast (
    val timestamp: String,
    val aqi: Double,
    val aqi_lower: Double,
    val aqi_upper: Double
)
package com.example.livelifebreatheair.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirQualityIndexApiResponse(
    @SerialName("air_quality")
    val airQuality: AirQualityData
)

@Serializable
data class AirQualityData(
    @SerialName("hoursInfo")
    val hoursInfo: List<HourInfo>,
    @SerialName("regionCode")
    val regionCode: String
)

@Serializable
data class HourInfo(
    val dateTime: String,
    val indexes: List<AirQualityIndex>,
    val pollutants: List<Pollutant>
)

@Serializable
data class AirQualityIndex(
    val code: String,
    val displayName: String,
    val aqi: Int,
    val aqiDisplay: String,
    val color: Color,
    val category: String,
    val dominantPollutant: String
)

@Serializable
data class Color(
    val red: Double,
    val green: Double,
    val blue: Double
)

@Serializable
data class Pollutant(
    val code: String,
    val displayName: String,
    val fullName: String,
    val concentration: Concentration
)

@Serializable
data class Concentration(
    val value: Double,
    val units: String
)
package com.example.livelifebreatheair.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val weather: Weather
)

@Serializable
data class Weather(
    val hourly: Hourly,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temperature2m: List<Double>,
    @SerialName("apparent_temperature")
    val apparentTemperature: List<Double>,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: List<Int>,
    @SerialName("wind_speed_10m")
    val windSpeed10m: List<Double>,
    @SerialName("weather_code")
    val weatherCode: List<Int>,
    @SerialName("uv_index")
    val uvIndex: List<Int>
)

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName("temperature_2m")
    val temperature2m: String,
    @SerialName("apparent_temperature")
    val apparentTemperature: String,
    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: String,
    @SerialName("wind_speed_10m")
    val windSpeed10m: String,
    @SerialName("weather_code")
    val weatherCode: String,
    @SerialName("uv_index")
    val uvIndex: String
)

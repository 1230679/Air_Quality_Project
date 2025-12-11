package com.example.livelifebreatheair.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponse(
    val weather: Weather,
    val nextPageToken: String = ""
)

@Serializable
data class Weather(
    val historyHours: List<HistoryHour>,
    val timeZone: TimeZone
)

@Serializable
data class HistoryHour(
    val interval: Interval,
    val displayDateTime: DisplayDateTime,
    val weatherCondition: WeatherCondition,
    val temperature: Temperature,
    val feelsLikeTemperature: Temperature,
    val dewPoint: Temperature,
    val heatIndex: Temperature,
    val windChill: Temperature,
    val wetBulbTemperature: Temperature,
    val precipitation: Precipitation,
    val airPressure: AirPressure,
    val wind: Wind,
    val visibility: Visibility,
    val iceThickness: IceThickness,
    val isDaytime: Boolean,
    val relativeHumidity: Int,
    val uvIndex: Int,
    val thunderstormProbability: Int,
    val cloudCover: Int
)

@Serializable
data class Interval(
    val startTime: String,
    val endTime: String
)

@Serializable
data class DisplayDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val nanos: Int,
    val utcOffset: String
)

@Serializable
data class WeatherCondition(
    val iconBaseUri: String,
    val description: Description,
    val type: String
)

@Serializable
data class Description(
    val text: String,
    val languageCode: String
)

@Serializable
data class Temperature(
    val unit: String,
    val degrees: Double
)

@Serializable
data class Precipitation(
    val probability: PrecipitationProbability,
    val snowQpf: Measurement,
    val qpf: Measurement
)

@Serializable
data class PrecipitationProbability(
    val type: String,
    val percent: Int
)

@Serializable
data class Measurement(
    val unit: String,
    val quantity: Double = 0.0,
    val thickness: Double = 0.0
)

@Serializable
data class AirPressure(
    val meanSeaLevelMillibars: Double
)

@Serializable
data class Wind(
    val direction: WindDirection,
    val speed: Speed,
    val gust: Speed
)

@Serializable
data class WindDirection(
    val cardinal: String,
    val degrees: Int
)

@Serializable
data class Speed(
    val unit: String,
    val value: Int
)

@Serializable
data class Visibility(
    val unit: String,
    val distance: Int
)

@Serializable
data class IceThickness(
    val unit: String,
    val thickness: Int
)

@Serializable
data class TimeZone(
    val id: String,
    val version: String = ""
)
package com.example.livelifebreatheair.ui.models

data class WeatherForecastItem(
    val label: String,      // "14:00"
    val condition: String   // "Cloudy"
)

data class WeatherScreenData(
    val temperature: String,   // "26°C"
    val description: String,       // paragraph text
    val windSpeed: String,          // "26km/h"
    val humidityPercentage: String,      // "40 %"
    val rainProbability: String,           // "40 %"
    val forecastItems: List<WeatherForecastItem> = emptyList()
)


package com.example.livelifebreatheair.ui.models

// Examples of what the data could lock like included
data class WeatherScreenData(
    val temperature: String,   // "26°C"
    val description: String,       // paragraph text
    val windSpeed: String,          // "26km/h"
    val humidityPercentage: String,      // "40 %"
    val rainProbability: String           // "40 %"
)


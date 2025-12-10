package com.example.livelifebreatheair.ui.models

// Example code included
data class PollutantCard(
    val name: String,          // "Carbon Monoxide"
    val value: String      // "10 mg/m³"
)

data class AirQualityForecastItem(
    val label: String,         // "index"
    val range: String      // "12-16"
)

data class AirQualityScreenData(
    val overallCategory: String,   // "Good"
    val index: String,         // "Index: 2"
    val description: String,
    val pollutantCards: List<PollutantCard>,
    val forecastItems: List<AirQualityForecastItem>
)
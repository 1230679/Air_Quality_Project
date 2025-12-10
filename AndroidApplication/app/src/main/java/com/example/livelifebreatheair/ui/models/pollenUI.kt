package com.example.livelifebreatheair.ui.models

// Examples included
data class PollenTypeCard(
    val name: String,          // "Oak", "Weeds"...
    val value: String      // "370 ppm"
)

data class PollenForecastItem(
    val label: String,         // "index"
    val range: String      // "12-16"
)

data class PollenScreenData(
    val pollenRange: String,   // "Middle"
    val pollenCount: String,  // "280 ppm"
    val description: String,
    val typeCards: List<PollenTypeCard>,
    val forecastItems: List<PollenForecastItem>
)

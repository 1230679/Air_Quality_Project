package com.example.livelifebreatheair.sampleData

import com.example.livelifebreatheair.ui.models.AirQualityForecastItem
import com.example.livelifebreatheair.ui.models.AirQualityScreenData
import com.example.livelifebreatheair.ui.models.PollenForecastItem
import com.example.livelifebreatheair.ui.models.PollenScreenData
import com.example.livelifebreatheair.ui.models.PollenTypeCard
import com.example.livelifebreatheair.ui.models.PollutantCard
import com.example.livelifebreatheair.ui.models.WeatherScreenData

object MockData {

    // Weather data
    val weatherScreen = WeatherScreenData(
        temperature = "26°C",
        description = "The air quality in Aarhus is currently good, meaning the overall air-pollution.",
        windSpeed = "26km/h",
        humidityPercentage = "40 %",
        rainProbability = "40 %"
    )

    // Pollen data
    val pollenScreen = PollenScreenData(
        pollenRange = "Middle",
        pollenCount = "280 ppm",
        description = "The air quality in Aarhus is currently good, meaning the overall air-pollution levels are low and pose little to no risk for the general population.",
        typeCards = listOf(
            PollenTypeCard("Oak", "370 ppm"),
            PollenTypeCard("Weeds", "20 ppm"),
            PollenTypeCard("Trees", "220 ppm"),
            PollenTypeCard("Grass", "60 ppm")
        ),
        forecastItems = listOf(
            PollenForecastItem("index", "12-16"),
            PollenForecastItem("index", "16-20"),
            PollenForecastItem("index", "20-24"),
            PollenForecastItem("index", "00-04")
        )
    )

    // Air Quality
    val airQualityScreen = AirQualityScreenData(
        overallCategory = "Good",
        index = "Index: 2",
        description = "The air quality in Aarhus is currently good, " +
                "meaning the overall air-pollution levels are low and pose little to no risk " +
                "for the general population.",
        pollutantCards = listOf(
            PollutantCard("Carbon Monoxide", "10 mg/m³"),
            PollutantCard("O₃", "100 µg/m³"),
            PollutantCard("PM2.5", "12 µg/m³"),
            PollutantCard("PM10", "8 µg/m³")
        ),
        forecastItems = listOf(
            AirQualityForecastItem("index", "12-16"),
            AirQualityForecastItem("index", "16-20"),
            AirQualityForecastItem("index", "20-24"),
            AirQualityForecastItem("index", "00-04")
        )
    )
}


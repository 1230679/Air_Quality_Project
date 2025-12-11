package com.example.livelifebreatheair.sampleData

import com.example.livelifebreatheair.ui.components.ChartData
import com.example.livelifebreatheair.ui.components.DataCategory
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

    // HISTORICAL CHART DATA

    // Labels for Charts
    val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val hourLabels = listOf("00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00")

    // Air Quality Charts
    val historicalCharts = listOf(
        // PM2.5 (Feinstaub)
        ChartData(
            id = "pm25",
            title = "PM2.5",
            category = DataCategory.AIR_QUALITY,
            values = listOf(8, 13, 16, 14, 15, 12, 9),
            labels = weekLabels,
            unit = "µg/m³"
        ),

        // PM10
        ChartData(
            id = "pm10",
            title = "PM10",
            category = DataCategory.AIR_QUALITY,
            values = listOf(15, 22, 28, 25, 27, 20, 18),
            labels = weekLabels,
            unit = "µg/m³"
        ),

        // Air Quality Index (AQI)
        ChartData(
            id = "aqi",
            title = "Air Quality Index",
            category = DataCategory.AIR_QUALITY,
            values = listOf(45, 52, 48, 55, 50, 47, 44),
            labels = weekLabels,
            unit = "AQI"
        ),

        // Ozon (O₃)
        ChartData(
            id = "ozone",
            title = "Ozone (O₃)",
            category = DataCategory.AIR_QUALITY,
            values = listOf(80, 95, 110, 105, 98, 85, 78),
            labels = weekLabels,
            unit = "µg/m³"
        ),

        // Carbon Monoxide (CO)
        ChartData(
            id = "co",
            title = "Carbon Monoxide",
            category = DataCategory.AIR_QUALITY,
            values = listOf(8, 10, 12, 11, 9, 7, 6),
            labels = weekLabels,
            unit = "mg/m³"
        ),

        // Weather Charts

        // Temperature
        ChartData(
            id = "temperature",
            title = "Temperature",
            category = DataCategory.WEATHER,
            values = listOf(18, 22, 25, 23, 20, 19, 21),
            labels = weekLabels,
            unit = "°C"
        ),

        // Humidity
        ChartData(
            id = "humidity",
            title = "Humidity",
            category = DataCategory.WEATHER,
            values = listOf(45, 50, 38, 42, 55, 60, 52),
            labels = weekLabels,
            unit = "%"
        ),

        // Windspeed
        ChartData(
            id = "windspeed",
            title = "Wind Speed",
            category = DataCategory.WEATHER,
            values = listOf(12, 18, 25, 22, 15, 10, 14),
            labels = weekLabels,
            unit = "km/h"
        ),

        // rain
        ChartData(
            id = "rain",
            title = "Rain Probability",
            category = DataCategory.WEATHER,
            values = listOf(10, 20, 40, 60, 35, 15, 5),
            labels = weekLabels,
            unit = "%"
        ),

        // Pollen Charts
        // Pollen
        ChartData(
            id = "pollen_total",
            title = "Total Pollen Count",
            category = DataCategory.POLLEN,
            values = listOf(280, 320, 370, 340, 300, 260, 240),
            labels = weekLabels,
            unit = "ppm"
        ),

        // Oak
        ChartData(
            id = "pollen_oak",
            title = "Oak Pollen",
            category = DataCategory.POLLEN,
            values = listOf(370, 420, 450, 400, 380, 340, 320),
            labels = weekLabels,
            unit = "ppm"
        ),

        //Grass
        ChartData(
            id = "pollen_grass",
            title = "Grass Pollen",
            category = DataCategory.POLLEN,
            values = listOf(60, 75, 85, 80, 70, 55, 50),
            labels = weekLabels,
            unit = "ppm"
        ),

        // Trees
        ChartData(
            id = "pollen_trees",
            title = "Tree Pollen",
            category = DataCategory.POLLEN,
            values = listOf(220, 260, 290, 270, 250, 210, 200),
            labels = weekLabels,
            unit = "ppm"
        ),

        // Weeds
        ChartData(
            id = "pollen_weeds",
            title = "Weed Pollen",
            category = DataCategory.POLLEN,
            values = listOf(20, 25, 32, 28, 22, 18, 15),
            labels = weekLabels,
            unit = "ppm"
        )
    )

    // Helperfunction: get Charts by Category
    fun getChartsByCategory(category: DataCategory): List<ChartData> {
        return if (category == DataCategory.ALL) {
            historicalCharts
        } else {
            historicalCharts.filter { it.category == category }
        }
    }

    // Helperfunction: get chart by ID
    fun getChartById(id: String): ChartData? {
        return historicalCharts.find { it.id == id }
    }
}


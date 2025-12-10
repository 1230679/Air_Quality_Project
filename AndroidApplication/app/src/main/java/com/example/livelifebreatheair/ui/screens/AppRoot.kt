package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import com.example.livelifebreatheair.data.model.AirQualityIndexApiResponse
import com.example.livelifebreatheair.data.model.PollenData
import com.example.livelifebreatheair.data.model.WeatherData
import com.example.livelifebreatheair.data.repository.ApiRepository
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.components.AirPollenTab
import com.example.livelifebreatheair.ui.models.AirQualityScreenData
import com.example.livelifebreatheair.ui.models.PollenScreenData
import com.example.livelifebreatheair.ui.models.WeatherScreenData
import com.example.livelifebreatheair.ui.models.AirQualityForecastItem
import com.example.livelifebreatheair.ui.models.PollenForecastItem
import com.example.livelifebreatheair.ui.models.PollenTypeCard
import com.example.livelifebreatheair.ui.models.PollutantCard

@Composable
fun AppRoot(
    repository: ApiRepository = ApiRepository()
) {
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

    // For toggling on and off the cards on the dashboard
    var hiddenAirMetrics by remember { mutableStateOf(setOf<String>()) }
    var hiddenPollenTypes by remember { mutableStateOf(setOf<String>()) }

    var airUiData by remember { mutableStateOf<AirQualityScreenData?>(null) }
    var pollenUiData by remember { mutableStateOf<PollenScreenData?>(null) }
    var weatherUiData by remember { mutableStateOf<WeatherScreenData?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch data once when the user has logged in and we show dashboards
    if (currentScreen != AppScreen.Login) {
        LaunchedEffect(Unit) {
            if (airUiData == null && pollenUiData == null && weatherUiData == null) {
                isLoading = true
                errorMessage = null

                val airResult = repository.getAirQualityData()
                val pollenResult = repository.getPollenData()
                val weatherResult = repository.getWeatherData()

                airResult
                    .onSuccess { api -> airUiData = api.toAirQualityScreenData() }
                    .onFailure { e -> errorMessage = (errorMessage ?: "") + "\nAir: ${e.message}" }

                pollenResult
                    .onSuccess { api -> pollenUiData = api.toPollenScreenData() }
                    .onFailure { e -> errorMessage = (errorMessage ?: "") + "\nPollen: ${e.message}" }

                weatherResult
                    .onSuccess { api -> weatherUiData = api.toWeatherScreenData() }
                    .onFailure { e -> errorMessage = (errorMessage ?: "") + "\nWeather: ${e.message}" }

                isLoading = false
            }
        }
    }

    if (currentScreen == AppScreen.Login) {
        LoginScreen(
            onLogin = { currentScreen = AppScreen.Air }
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                if (isLoading && airUiData == null && pollenUiData == null && weatherUiData == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading data…")
                    }
                } else {
                    when (currentScreen) {
                        AppScreen.Air -> {
                            val base = airUiData ?: MockData.airQualityScreen
                            val filtered = base.copy(
                                pollutantCards = base.pollutantCards.map { card ->
                                    if (card.name in hiddenAirMetrics) {
                                        card.copy(value = "")
                                    } else {
                                        card
                                    }
                                }
                            )

                            AirQualityDashboardScreen(
                                onProfileClick = { currentScreen = AppScreen.Profile },
                                data = filtered,
                                hiddenMetrics = hiddenAirMetrics
                            )
                        }

                        AppScreen.Weather -> {
                            val base = weatherUiData ?: MockData.weatherScreen
                            WeatherDashboardScreen(
                                onProfileClick = { currentScreen = AppScreen.Profile },
                                data = base
                            )
                        }

                        AppScreen.Pollen -> {
                            val base = pollenUiData ?: MockData.pollenScreen
                            val filtered = base.copy(
                                typeCards = base.typeCards.map { card ->
                                    if (card.name in hiddenPollenTypes) {
                                        card.copy(value = "")
                                    } else {
                                        card
                                    }
                                }
                            )
                            PollenDashboardScreen(
                                onProfileClick = { currentScreen = AppScreen.Profile },
                                data = filtered,
                                hiddenTypes = hiddenPollenTypes
                            )
                        }

                        AppScreen.History -> HistoricalDataScreen(
                            onProfileClick = { currentScreen = AppScreen.Profile }
                        )

                        AppScreen.Profile -> {
                            ProfileScreen(
                                onBackClick = { currentScreen = AppScreen.Air },
                                hiddenAirMetrics = hiddenAirMetrics,
                                hiddenPollenTypes = hiddenPollenTypes,
                                onToggleAirMetric = { name ->
                                    hiddenAirMetrics =
                                        if (name in hiddenAirMetrics)
                                            hiddenAirMetrics - name
                                        else
                                            hiddenAirMetrics + name
                                },
                                onTogglePollenType = { name ->
                                    hiddenPollenTypes =
                                        if (name in hiddenPollenTypes)
                                            hiddenPollenTypes - name
                                        else
                                            hiddenPollenTypes + name
                                },
                                airData = airUiData,
                                pollenData = pollenUiData
                            )
                        }

                        AppScreen.Login -> { /* handled above */ }
                    }
                }
            }

            if (currentScreen != AppScreen.Profile) {
                AirPollenTab(
                    selected = currentScreen,
                    onSelected = { currentScreen = it }
                )
            }
        }
    }
}

private fun AirQualityIndexApiResponse.toAirQualityScreenData(): AirQualityScreenData {
    val firstHour = airQuality.firstOrNull() ?: return MockData.airQualityScreen

    val mainIndex = firstHour.indexes.firstOrNull()
    val category = mainIndex?.category ?: "Unknown"
    val indexValue = mainIndex?.aqi?.toString() ?: "--"

    val pollutantByCode = firstHour.pollutants.associateBy { it.code.lowercase() }

    val pollutantCards = listOf(
        "Carbon Monoxide" to "co",
        "O₃" to "o3",
        "PM2.5" to "pm2_5",
        "PM10" to "pm10"
    ).map { (label, code) ->
        val pollutant = pollutantByCode[code]
        val value = pollutant?.let {
            "${it.concentration.value} ${it.concentration.units}"
        } ?: "--"
        PollutantCard(
            name = label,
            value = value
        )
    }

    val forecastItems = airQuality.take(4).map { hour ->
        val hourPart = hour.dateTime.substringAfter("T", hour.dateTime)
        val idx = hour.indexes.firstOrNull()
        AirQualityForecastItem(
            label = hourPart,
            range = idx?.aqiDisplay ?: "--"
        )
    }

    return AirQualityScreenData(
        overallCategory = category,
        index = "Index: $indexValue",
        description = "The air quality is currently $category in your area.",
        pollutantCards = pollutantCards,
        forecastItems = forecastItems
    )
}

private fun PollenData.toPollenScreenData(): PollenScreenData {
    val firstDay = pollen.dailyInfo.firstOrNull() ?: return MockData.pollenScreen

    val types = firstDay.pollenTypeInfo
    if (types.isEmpty()) return MockData.pollenScreen

    // Choose the "worst" type as overall
    val worst = types.maxByOrNull { it.indexInfo.value } ?: types.first()

    val typeCards = types.map { type ->
        PollenTypeCard(
            name = type.displayName,
            value = "${type.indexInfo.value} index"
        )
    }

    val forecastItems = pollen.dailyInfo.take(4).map { day ->
        val code = day.pollenTypeInfo
            .maxByOrNull { it.indexInfo.value }
            ?.indexInfo
        PollenForecastItem(
            label = "${day.date.day}/${day.date.month}",
            range = code?.category ?: "N/A"
        )
    }

    val totalIndex = types.sumOf { it.indexInfo.value }

    return PollenScreenData(
        pollenRange = worst.indexInfo.category,
        pollenCount = "$totalIndex index",
        description = worst.indexInfo.indexDescription,
        typeCards = typeCards,
        forecastItems = forecastItems
    )
}

private fun WeatherData.toWeatherScreenData(): WeatherScreenData {
    val hourly = weather.hourly
    val units = weather.hourlyUnits

    val i = 0

    val temp = hourly.temperature2m.getOrNull(i) ?: 0.0
    val feelsLike = hourly.apparentTemperature.getOrNull(i) ?: temp
    val humidity = hourly.relativeHumidity2m.getOrNull(i) ?: 0
    val wind = hourly.windSpeed10m.getOrNull(i) ?: 0.0
    val uv = hourly.uvIndex.getOrNull(i) ?: 0

    return WeatherScreenData(
        temperature = "${temp.toInt()}${units.temperature2m}",
        description = "Feels like ${feelsLike.toInt()}${units.apparentTemperature}. UV index: $uv.",
        windSpeed = "${wind.toInt()}${units.windSpeed10m}",
        humidityPercentage = "$humidity ${units.relativeHumidity2m}",
        rainProbability = "UV $uv"
    )
}




package com.example.livelifebreatheair.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.livelifebreatheair.HistoricalDataScreen
import com.example.livelifebreatheair.data.model.AirQualityIndexApiResponse
import com.example.livelifebreatheair.data.model.AirQualityIndexForecastResponse
import com.example.livelifebreatheair.data.model.PollenData
import com.example.livelifebreatheair.data.model.WeatherApiResponse
import com.example.livelifebreatheair.data.repository.ApiRepository
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.components.AirPollenTab
import com.example.livelifebreatheair.ui.models.AirQualityForecastItem
import com.example.livelifebreatheair.ui.models.AirQualityScreenData
import com.example.livelifebreatheair.ui.models.PollenForecastItem
import com.example.livelifebreatheair.ui.models.PollenScreenData
import com.example.livelifebreatheair.ui.models.PollenTypeCard
import com.example.livelifebreatheair.ui.models.PollutantCard
import com.example.livelifebreatheair.ui.models.WeatherForecastItem
import com.example.livelifebreatheair.ui.models.WeatherScreenData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.text.format
import kotlin.text.toInt

@RequiresApi(Build.VERSION_CODES.O)
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
                //val weatherResult = repository.getWeatherData()
                val weatherResult: Result<WeatherApiResponse> = repository.getWeatherData()
                val aqiForecastResult: Result<AirQualityIndexForecastResponse> = repository.getAirQualityIndexForecast()

                aqiForecastResult
                    .onSuccess { /* nothing to do here */ }
                    .onFailure { e ->
                        errorMessage = (errorMessage ?: "") + "\nAQI Forecast: ${e.message}"
                    }

                val aqiForecastData = aqiForecastResult
                    .getOrNull()
                    ?.toAirQualityForecastData()
                    ?: emptyList()

                airResult
                    .onSuccess { api -> airUiData = api.toAirQualityScreenData(aqiForecastData) }
                    .onFailure { e -> errorMessage = (errorMessage ?: "") + "\nAir: ${e.message}" }

                pollenResult
                    .onSuccess { api -> pollenUiData = api.toPollenScreenData() }
                    .onFailure { e -> errorMessage = (errorMessage ?: "") + "\nPollen: ${e.message}" }

                weatherResult
                    .onSuccess { api: WeatherApiResponse ->
                        weatherUiData = api.toWeatherScreenData()
                    }
                    .onFailure { e ->
                        errorMessage = (errorMessage ?: "") + "\nWeather: ${e.message}"
                    }

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
                            // onProfileClick = { currentScreen = AppScreen.Profile }
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

// Mapping from the API to the UI models

private fun formatConcentrationUnits(units: String): String {
    return when (units.uppercase()) {
        "PARTS_PER_BILLION" -> "ppb"
        "PARTS_PER_MILLION" -> "ppm"
        "MICROGRAMS_PER_CUBIC_METER" -> "µg/m³"
        "MILLIGRAMS_PER_CUBIC_METER" -> "mg/m³"
        else -> units.replace("_", " ").lowercase()
    }
}

private fun formatTemperature(temp: com.example.livelifebreatheair.data.model.Temperature): String {
    val symbol = when (temp.unit.uppercase()) {
        "CELSIUS" -> "°C"
        "FAHRENHEIT" -> "°F"
        else -> "°"
    }
    return "${temp.degrees.toInt()}$symbol"
}

private fun formatWind(speed: com.example.livelifebreatheair.data.model.Speed): String {
    val unit = when (speed.unit.uppercase()) {
        "KILOMETERS_PER_HOUR" -> "km/h"
        "MILES_PER_HOUR" -> "mph"
        "METERS_PER_SECOND" -> "m/s"
        else -> speed.unit.lowercase()
    }
    return "${speed.value} $unit"
}

// --- Air quality ----------------------------------------------------------

private fun AirQualityIndexApiResponse.toAirQualityScreenData(forecastItems: List<AirQualityForecastItem>): AirQualityScreenData {
    val firstHour = airQuality.hoursInfo.firstOrNull() ?: return MockData.airQualityScreen

    val mainIndex = firstHour.indexes.firstOrNull()
    val rawCategory = mainIndex?.category ?: "Unknown"

    // Turn "Good air quality" -> "Good"
    val cleanCategory = rawCategory
        .replace("air quality", "", ignoreCase = true)
        .trim()
        .ifEmpty { rawCategory }

    val indexValue = mainIndex?.aqi?.toString() ?: "--"

    val pollutantByCode = firstHour.pollutants.associateBy { it.code.lowercase() }

    // Fixed order to match dashboard + profile
    val pollutantCards = listOf(
        "Carbon Monoxide" to "co",
        "O₃" to "o3",
        "PM2.5" to "pm2_5",
        "PM10" to "pm10"
    ).map { (label, code) ->
        val pollutant = pollutantByCode[code]
        val valueText = pollutant?.let {
            val prettyUnits = formatConcentrationUnits(it.concentration.units)
            val roundedValue = String.format(java.util.Locale.getDefault(), "%.0f", it.concentration.value)
            "$roundedValue $prettyUnits"
        } ?: "--"
        PollutantCard(
            name = label,
            value = valueText
        )
    }

    return AirQualityScreenData(
        overallCategory = cleanCategory,
        index = "Index: $indexValue",
        description = "The air quality is currently ${cleanCategory.lowercase()} in your area.",
        pollutantCards = pollutantCards,
        forecastItems = forecastItems
    )
}

// --- AirQualityForecast --------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
private fun AirQualityIndexForecastResponse.toAirQualityForecastData(): List<AirQualityForecastItem> {

    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    // Parser for timestamps without timezone
    val localParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    return entries.take(4).map { entry ->
        val label = run {
            val isoZoned = runCatching {
                Instant.parse(entry.timestamp).atZone(ZoneId.systemDefault())
            }.getOrNull()

            if (isoZoned != null)
                return@run isoZoned.format(formatter)

            val local = runCatching {
                LocalDateTime.parse(entry.timestamp, localParser)
            }.getOrNull()

            if (local != null)
                return@run local.format(formatter)

            val match = Regex("""\b(\d{2}):(\d{2})""")
                .find(entry.timestamp)
                ?.value

            match ?: "--"
        }

        AirQualityForecastItem(
            label = label,
            range = entry.aqi?.toInt()?.toString() ?: "--"
        )
    }
}



// --- Pollen ---------------------------------------------------------------

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
        val idx = day.pollenTypeInfo
            .maxByOrNull { it.indexInfo.value }
            ?.indexInfo
        PollenForecastItem(
            label = "${day.date.day}/${day.date.month}",
            range = idx?.category ?: "N/A"
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

// --- Weather --------------------------------------------------------------

private fun WeatherApiResponse.toWeatherScreenData(): WeatherScreenData {
    val hours = weather.historyHours
    val current = hours.firstOrNull() ?: return MockData.weatherScreen

    fun formatTime(hour: com.example.livelifebreatheair.data.model.HistoryHour): String {
        val h = hour.displayDateTime.hours.toString().padStart(2, '0')
        val m = hour.displayDateTime.minutes.toString().padStart(2, '0')
        return "$h:$m"
    }

    val tempLabel = formatTemperature(current.temperature)
    val feelsLikeLabel = formatTemperature(current.feelsLikeTemperature)
    val humidity = current.relativeHumidity
    val windLabel = formatWind(current.wind.speed)
    val rainProb = current.precipitation.probability.percent

    // Build 4 forecast items from the next 4 hours (including current)
    val forecastItems = hours.take(4).map { hour ->
        WeatherForecastItem(
            label = formatTime(hour),
            condition = hour.weatherCondition.description.text
        )
    }

    return WeatherScreenData(
        temperature = tempLabel,
        description = buildString {
            append(current.weatherCondition.description.text)
            append(". Feels like $feelsLikeLabel.")
            append(" UV index: ${current.uvIndex}.")
        },
        windSpeed = windLabel,
        humidityPercentage = "$humidity %",
        rainProbability = "$rainProb %",
        forecastItems = forecastItems
    )
}

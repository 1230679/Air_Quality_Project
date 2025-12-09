package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.components.AirPollenTab


@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

    var hiddenAirMetrics by remember { mutableStateOf(setOf<String>()) }
    var hiddenPollenTypes by remember { mutableStateOf(setOf<String>()) }

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
                when (currentScreen) {
                    AppScreen.Air -> {
                        // TODO: Update later
                        val base = MockData.airQualityScreen
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

                    AppScreen.Weather -> WeatherDashboardScreen(
                        onProfileClick = { currentScreen = AppScreen.Profile }
                    )

                    AppScreen.Pollen -> {
                        val base = MockData.pollenScreen
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

                    AppScreen.Profile -> ProfileScreen(
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
                        }
                    )

                    AppScreen.Login -> { /* wordt hierboven al afgehandeld */ }
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


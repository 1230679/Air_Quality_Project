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
import com.example.livelifebreatheair.ui.components.AirPollenTab
import com.example.livelifebreatheair.ui.screens.PollenDashboardScreen
import com.example.livelifebreatheair.ui.screens.HistoricalDataScreen


@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }

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
                    AppScreen.Air -> AirQualityDashboardScreen(
                        onProfileClick = { currentScreen = AppScreen.Profile }
                    )

                    AppScreen.Weather -> WeatherDashboardScreen(
                        onProfileClick = { currentScreen = AppScreen.Profile }
                    )

                    AppScreen.Pollen -> PollenDashboardScreen(
                        onProfileClick = { currentScreen = AppScreen.Profile }
                    )

                    AppScreen.History -> HistoricalDataScreen(
                        onProfileClick = { currentScreen = AppScreen.Profile }
                    )

                    AppScreen.Profile -> ProfileScreen(
                        onBackClick = { currentScreen = AppScreen.Air }
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


package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.livelifebreatheair.data.model.AirQualityIndexApiResponse
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.components.LoadingView
import com.example.livelifebreatheair.ui.models.AirQualityScreenData
import com.example.livelifebreatheair.viewModel.HistoricalDataViewModel
import com.example.livelifebreatheair.viewModel.HistoricalDataViewModelFactory


@Composable
fun AirQualityDashboardScreen(
    onProfileClick: () -> Unit = {},
    hiddenMetrics: Set<String> = emptySet()
) {
    val viewModel: HistoricalDataViewModel = viewModel(factory = HistoricalDataViewModelFactory())
    LaunchedEffect(Unit) {
        viewModel.loadAirQuality()
    }
    val data by viewModel.airQuality.collectAsState()

    if(data == null) {
        LoadingView()
        return
    }

    val nonNullData: Result<AirQualityIndexApiResponse> = data!!
    val dataValue: AirQualityIndexApiResponse? = nonNullData.getOrNull()
    val mockData: AirQualityScreenData = MockData.airQualityScreen

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFCCECFF),
                        Color(0xFFA6D3FF),
                        Color(0xFF88ACFF)
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header(onProfileClick = onProfileClick)

            MainStatusCard(dataValue!!)

            PollutantsGrid(
                data = dataValue,
                hiddenMetrics = hiddenMetrics
            )

            ForecastRow(mockData)
        }
    }
}

@Composable
private fun Header(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Air quality",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C2433)
            )
        )

        Surface(
            onClick = onProfileClick,
            shape = CircleShape,
            color = Color(0xFFFFF4E4),
            modifier = Modifier
                .size(38.dp)
                .shadow(8.dp, CircleShape, clip = false)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("U", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MainStatusCard(
    data: AirQualityIndexApiResponse
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(32.dp), clip = false)
            .clip(RoundedCornerShape(32.dp)),
        color = Color(0xE6F3F7FF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(180.dp),
                    shape = CircleShape,
                    color = Color(0xFFE2F7E8)
                ) {}

                Surface(
                    modifier = Modifier.size(150.dp),
                    shape = CircleShape,
                    color = Color.White
                ) {}

                Surface(
                    modifier = Modifier.size(130.dp),
                    shape = CircleShape,
                    color = Color.Transparent,
                    border = BorderStroke(10.dp, Color(0xFF33B34A))
                ) {}

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = data.airQuality.hoursInfo[0].indexes[0].category,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C2433)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = data.airQuality.hoursInfo[0].indexes[0].aqiDisplay,
                        fontSize = 14.sp,
                        color = Color(0xFF4C5C6E)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = data.airQuality.hoursInfo[0].indexes[0].dominantPollutant,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF4C5C6E)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PollutantsGrid(
    data: AirQualityIndexApiResponse,
    hiddenMetrics: Set<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val co = data.airQuality.hoursInfo[0].pollutants[0]
            if (co.fullName !in hiddenMetrics) {
                PollutantCard(
                    label = co.fullName,
                    value = co.concentration.value.toString(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            val o3 = data.airQuality.hoursInfo[0].pollutants[1]
            if (o3.fullName !in hiddenMetrics) {
                PollutantCard(
                    label = o3.fullName,
                    value = o3.concentration.value.toString(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val pm25 = data.airQuality.hoursInfo[0].pollutants[2]
            if (pm25.fullName !in hiddenMetrics) {
                PollutantCard(
                    label = pm25.fullName,
                    value = pm25.concentration.value.toString(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            val pm10 = data.airQuality.hoursInfo[0].pollutants[3]
            if (pm10.fullName !in hiddenMetrics) {
                PollutantCard(
                    label = pm10.fullName,
                    value = pm10.concentration.value.toString(),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PollutantCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(90.dp)
            .shadow(12.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp)),
        color = Color(0xE6F3F7FF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF404040)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF808080)
            )
        }
    }
}

@Composable
private fun ForecastRow(
    data: AirQualityScreenData
) {
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            text = "Forecast",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C2433)
            )
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            data.forecastItems.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFE9F7EC),
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .shadow(10.dp, RoundedCornerShape(20.dp), clip = false)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            color = Color(0xFF4C5C6E)
                        )
                        Text(
                            text = item.range,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C2433)
                        )
                    }
                }
            }
        }
    }
}

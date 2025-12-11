package com.example.livelifebreatheair.ui.screens

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
import com.example.livelifebreatheair.ui.models.WeatherForecastItem
import com.example.livelifebreatheair.ui.models.WeatherScreenData


@Composable
fun WeatherDashboardScreen(
    onProfileClick: () -> Unit = {},
    data: WeatherScreenData
) {
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
            WeatherHeader(onProfileClick = onProfileClick)

            WeatherMainCard(data)

            WeatherDetailsRow(data)

            WeatherForecastRow(data)
        }
    }
}



@Composable
private fun WeatherHeader(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Weather",
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
private fun WeatherMainCard(
    data: WeatherScreenData
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
            WeatherIcon()

            Spacer(Modifier.height(16.dp))

            Text(
                text = data.temperature,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C2433)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = data.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF4C5C6E)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun WeatherIcon() {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.TopEnd)
                .padding(end = 4.dp, top = 4.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD94D))
        )

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 4.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
private fun WeatherDetailsRow(
    data: WeatherScreenData
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp)),
        color = Color(0xE6F3F7FF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherDetailItem(
                label = "Wind",
                value = data.windSpeed
            )
            WeatherDetailItem(
                label = "Humidity",
                value = data.humidityPercentage
            )
            WeatherDetailItem(
                label = "Rain",
                value = data.rainProbability
            )
        }
    }
}

@Composable
private fun WeatherDetailItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF4C5C6E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C2433)
        )
    }
}

@Composable
private fun WeatherForecastRow(
    data: WeatherScreenData
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
            // If the list is empty, keep the old 4 placeholders so UI doesn't look broken
            val items = if (data.forecastItems.isNotEmpty()) {
                data.forecastItems.take(4)
            } else {
                List(4) { WeatherForecastItem(label = "", condition = "Cloudy") }
            }

            items.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xE6F3F7FF),
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .shadow(10.dp, RoundedCornerShape(20.dp), clip = false)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Only displays weather cloud for now
                        WeatherCloudMini()
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = item.condition,
                            fontSize = 11.sp,
                            color = Color(0xFF4C5C6E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherCloudMini() {
    Box(
        modifier = Modifier.size(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.TopEnd)
                .padding(end = 2.dp, top = 2.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD94D))
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

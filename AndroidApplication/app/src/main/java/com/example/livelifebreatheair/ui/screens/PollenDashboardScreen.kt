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
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.models.PollenScreenData

@Composable
fun PollenDashboardScreen(
    onProfileClick: () -> Unit = {},
    data: PollenScreenData = MockData.pollenScreen
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
            PollenHeader(onProfileClick = onProfileClick)

            PollenMainCard(data)

            PollenTypesGrid(data)

            PollenForecastRow(data)
        }
    }
}

@Composable
private fun PollenHeader(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pollen",
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
private fun PollenMainCard(
    data: PollenScreenData
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
                    color = Color(0xFFFFF5D6)
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
                    border = BorderStroke(10.dp, Color(0xFFFFCC33))
                ) {}

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = data.pollenRange,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C2433)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = data.pollenCount,
                        fontSize = 14.sp,
                        color = Color(0xFF4C5C6E)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

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
private fun PollenTypesGrid(
    data: PollenScreenData
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenTypeCard(
                label = data.typeCards[0].name,
                value = data.typeCards[0].value,
                borderColor = Color(0xFFFF5555),
                modifier = Modifier.weight(1f)
            )
            PollenTypeCard(
                label = data.typeCards[1].name,
                value = data.typeCards[1].value,
                borderColor = Color(0xFF55C94A),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenTypeCard(
                label = data.typeCards[2].name,
                value = data.typeCards[2].value,
                borderColor = Color(0xFF55C94A),
                modifier = Modifier.weight(1f)
            )
            PollenTypeCard(
                label = data.typeCards[3].name,
                value = data.typeCards[3].value,
                borderColor = Color(0xFF55C94A),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PollenTypeCard(
    label: String,
    value: String,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .shadow(12.dp, RoundedCornerShape(22.dp), clip = false)
            .clip(RoundedCornerShape(22.dp)),
        color = Color(0xE6F3F7FF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(4.dp, borderColor)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = value,
                        fontSize = 11.sp,
                        color = Color(0xFF1C2433),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4C5C6E)
            )
        }
    }
}

@Composable
private fun PollenForecastRow(
    data: PollenScreenData
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
            data.forecastItems.forEachIndexed { index, item ->
                val bgColor =
                    if (index == 1) Color(0xFFFFE1E1) else Color(0xFFE9F7EC)

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = bgColor,
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

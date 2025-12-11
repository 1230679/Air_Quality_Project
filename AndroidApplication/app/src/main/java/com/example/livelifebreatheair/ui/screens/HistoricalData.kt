package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width


@Composable
fun HistoricalDataScreen(
    onProfileClick: () -> Unit = {}
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
            HistoryHeader(onProfileClick = onProfileClick)

            HistoryFilterChips()

            Text(
                text = "PM2.5",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF1C2433)
                )
            )

            ChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                BarChartPlaceholder()
            }

            Text(
                text = "Air quality index",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF1C2433)
                )
            )

            ChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AreaChartPlaceholder()
            }

            Text(
                text = "PM2.5",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF1C2433)
                )
            )

            ChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                BarChartPlaceholder()
            }
        }
    }
}

@Composable
private fun HistoryHeader(
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Historical data",
            style = MaterialTheme.typography.titleLarge.copy(
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
                Text("U", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun HistoryFilterChips() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryChip("All", selected = true)
        HistoryChip("Air quality")
        HistoryChip("Weather")
        HistoryChip("Pollen")
    }
}

@Composable
private fun HistoryChip(
    label: String,
    selected: Boolean = false
) {
    val bg = if (selected) Color(0xFF1C5FFF) else Color(0x33FFFFFF)
    val fg = if (selected) Color.White else Color(0xFF1C2433)

    Surface(
        shape = RoundedCornerShape(50),
        color = bg,
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = label, color = fg, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ChartCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(32.dp), clip = false)
            .clip(RoundedCornerShape(32.dp)),
        color = Color(0xE6F3F7FF)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            content()
        }
    }
}

@Composable
private fun BarChartPlaceholder() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val heights = listOf(70, 120, 160, 140, 150, 110, 80)
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(18.dp)
                    .height(h.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF5E8CFF))
            )
        }
    }
}

@Composable
private fun AreaChartPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x335E8CFF))
    )
}

package com.example.livelifebreatheair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livelifebreatheair.ui.components.AirPollenTab
import com.example.livelifebreatheair.ui.theme.LiveLifeBreatheAirTheme

class HistoricalData: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveLifeBreatheAirTheme {
                HistoricalDataScreen()
            }
        }
    }
}

@Composable
fun HistoricalDataScreen() {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        cs.surface,
                        cs.background,
                    )
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Historical data",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )

                // round "menu" button on the right
                Surface(
                    shape = CircleShape,
                    color = cs.primaryContainer,
                    tonalElevation = 2.dp,
                    onClick = { /* TODO later */ },
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ---------- First chart ----------
            Text(
                text = "PM2.5",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(12.dp))

            ChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                // placeholder bar chart
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                   Column(
                       modifier = Modifier.fillMaxHeight(),
                       verticalArrangement = Arrangement.SpaceBetween
                   ) {
                       val steps = listOf(16, 14, 12, 10, 8, 6, 4, 2, 0)
                       steps.forEach {
                           Text(
                               text = it.toString(),
                               fontSize = 12.sp,
                               color = Color.Gray
                           )
                        }
                    }
                   Row(
                       Modifier
                           .fillMaxHeight()
                           .weight(1f),
                       verticalAlignment = Alignment.Bottom,
                       horizontalArrangement = Arrangement.SpaceBetween
                   )
                   {
                       val heights = listOf(80, 130, 160, 140, 150, 120, 90)
                       heights.forEach { h ->
                           Box(
                               Modifier
                                   .width(16.dp)
                                   .height(h.dp)
                                   .clip(RoundedCornerShape(4.dp))
                                   .background(cs.primaryContainer)
                           )
                       }
                   }
                }
            }

            Spacer(Modifier.height(36.dp))

            // Second chart
            Text(
                text = "Air quality index",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))

            ChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // very simple blue area placeholder
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(cs.primary.copy(alpha = 0.5f))
                    )
                }
            }

            // push the tab to the bottom
            Spacer(Modifier.weight(1f))

            AirPollenTab()

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ChartCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

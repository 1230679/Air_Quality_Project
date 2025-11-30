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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.components.AirPollenTab
import com.example.livelifebreatheair.ui.components.ChartData
import com.example.livelifebreatheair.ui.components.DataCategory
import com.example.livelifebreatheair.ui.components.FilterChips
import com.example.livelifebreatheair.ui.theme.LiveLifeBreatheAirTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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

    //state for selected filter
    var selectedCategories by remember {mutableStateOf(setOf(DataCategory.ALL))}

    // mockData
    val allCharts = MockData.historicalCharts

    //filter chart on selected categories
    val filteredCharts = if (selectedCategories.contains(DataCategory.ALL)) {
        allCharts
    } else {
        allCharts.filter { chart ->
            selectedCategories.contains(chart.category)
        }
    }

    // Handle Filter Toggle
    fun handleCategoryToggle(category: DataCategory) {
        selectedCategories = if (category == DataCategory.ALL) {
            setOf(DataCategory.ALL)
        } else {
            val newSelection = selectedCategories.toMutableSet()
            newSelection.remove(DataCategory.ALL)

            if (newSelection.contains(category)) {
                newSelection.remove(category)
                if (newSelection.isEmpty()) {
                    setOf(DataCategory.ALL)
                } else {
                    newSelection
                }
            } else {
                newSelection.add(category)
                newSelection
            }
        }
    }

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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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

            // Filter Chips
            FilterChips(
                selectedCategories = selectedCategories,
                onCategoryToggle = ::handleCategoryToggle,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // dynamic filtered Charts
            filteredCharts.forEach { chartData ->
                Text(
                    text = chartData.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(12.dp))

                BarChart(
                    data = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(Modifier.height(24.dp))
            }

            // push the tab to the bottom
            Spacer(Modifier.height(60.dp))



            /*// ---------- First chart ----------
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
            }*/

            // push the tab to the bottom
            Spacer(Modifier.weight(1f))

            //AirPollenTab()

            //Spacer(Modifier.height(8.dp))
        }
    }
}


@Composable
fun BarChart(
    data: ChartData,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    ChartCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val dataValues = data.values

        // calculates min and max for y-axis
        val maxValue = (dataValues.maxOrNull() ?: 16)
        val minValue = 0

        // calculates y-axis steps
        val stepCount = 9
        val range = maxValue - minValue
        val stepSize = (range / (stepCount - 1).toFloat())
        val yAxisLabels = (0 until stepCount).map {
            (it * stepSize).toInt()
        }

        // konverts datavalues to pixel-height
        val maxHeight = 180.dp
        val heights = dataValues.map { value ->
            (value.toFloat() / maxValue * maxHeight.value).dp
        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Y-axis labels
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(32.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                /*// unit at the top
                Text(
                    text = data.unit,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSecondaryContainer
                )*/

                // Labels, reversed to start from bottom
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    yAxisLabels.reversed().forEach { label ->
                        Text(
                            text = label.toString(),
                            fontSize = 12.sp,
                            color = Color.Gray //CHANGE
                        )
                    }
                }
            }

            // Bar chart
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Bar Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        heights.forEach { h ->
                            Box(
                                Modifier
                                    .width(24.dp)
                                    .height(h)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(cs.primaryContainer)
                            )
                        }
                    }
                }
            }
            /*// X-axis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.labels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }*/
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            content = content
        )
    }
}

package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.WindPower
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livelifebreatheair.sampleData.MockData
import com.example.livelifebreatheair.ui.models.AirQualityScreenData
import com.example.livelifebreatheair.ui.models.PollenScreenData

enum class AdjustmentTab { AIR, POLLEN }

@Composable
    fun ProfileScreen(
    onBackClick: () -> Unit = {},
    hiddenAirMetrics: Set<String>,
    hiddenPollenTypes: Set<String>,
    onToggleAirMetric: (String) -> Unit,
    onTogglePollenType: (String) -> Unit,
    airData: AirQualityScreenData? = null,
    pollenData: PollenScreenData? = null
    ) {

    var selectedTab by remember { mutableStateOf(AdjustmentTab.AIR) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2D2D2D)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
//                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFEAF6FF),
                                Color(0xFFD7E9FA)
                            )
                        )
                    )
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF404040)
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onBackClick() }
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Back to dashboard",
                            tint = Color(0xFF404040)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Welcome
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF404040)
                )
                Text(
                    text = "username!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF404040)
                )

                Spacer(Modifier.height(20.dp))

                // Personal data + Settings cards
                ProfileMenuItem(title = "Personal data")
                Spacer(Modifier.height(12.dp))
                ProfileMenuItem(title = "Settings", icon = Icons.Default.Settings)

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Adjustments",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF404040)
                )

                Spacer(Modifier.height(12.dp))

                // Segmented control
                AdjustmentSegmentedControl(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                Spacer(Modifier.height(20.dp))

                // Content
                when (selectedTab) {
                    AdjustmentTab.AIR -> AirAdjustments(
                        hiddenAirMetrics = hiddenAirMetrics,
                        onToggleAirMetric = onToggleAirMetric,
                        airData = airData ?: MockData.airQualityScreen
                    )
                    AdjustmentTab.POLLEN -> PollenAdjustments(
                        hiddenPollenMetrics = hiddenPollenTypes,
                        onTogglePollenType = onTogglePollenType,
                        pollenData = pollenData ?: MockData.pollenScreen
                    )
                }


                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

// Top menu items
@Composable
private fun ProfileMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.AutoMirrored.Filled.ArrowForward
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF404040)
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF808080)
            )
        }
    }
}

// Segmented control

@Composable
private fun AdjustmentSegmentedControl(
    selectedTab: AdjustmentTab,
    onTabSelected: (AdjustmentTab) -> Unit
) {
    val background = Color(0xFFE4EDF5)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SegmentButton(
            modifier = Modifier.weight(1f),
            selected = selectedTab == AdjustmentTab.AIR,
            onClick = { onTabSelected(AdjustmentTab.AIR) }
        ) {
            Icon(
                imageVector = Icons.Outlined.WindPower,
                contentDescription = "Air",
                tint = if (selectedTab == AdjustmentTab.AIR) Color(0xFF404040) else Color(0xFFB0B0B0)
            )
        }

        Spacer(Modifier.width(4.dp))

        SegmentButton(
            modifier = Modifier.weight(1f),
            selected = selectedTab == AdjustmentTab.POLLEN,
            onClick = { onTabSelected(AdjustmentTab.POLLEN) }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Pollen",
                tint = if (selectedTab == AdjustmentTab.POLLEN) Color(0xFF404040) else Color(0xFFB0B0B0)
            )
        }
    }
}

@Composable
private fun SegmentButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

// Air layout
@Composable
private fun AirAdjustments(
    hiddenAirMetrics: Set<String>,
    onToggleAirMetric: (String) -> Unit,
    airData: AirQualityScreenData
) {
    val byName = airData.pollutantCards.associateBy { it.name }

    fun splitValueUnit(raw: String): Pair<String, String> {
        val parts = raw.trim().split(" ")
        if (parts.isEmpty()) return "" to ""
        if (parts.size == 1) return parts[0] to ""
        return parts[0] to parts.drop(1).joinToString(" ")
    }

    val (coValue, coUnit) = splitValueUnit(byName["Carbon Monoxide"]?.value ?: "10 mg/m³")
    val (o3Value, o3Unit) = splitValueUnit(byName["O₃"]?.value ?: "100 µg/m³")
    val (pm25Value, pm25Unit) = splitValueUnit(byName["PM2.5"]?.value ?: "12 µg/m³")
    val (pm10Value, pm10Unit) = splitValueUnit(byName["PM10"]?.value ?: "8 µg/m³")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AirCard(
                value = coValue,
                unit = coUnit,
                label = "Carbon Monoxide",
                isEnabled = "Carbon Monoxide" !in hiddenAirMetrics,
                onPlusClick = { onToggleAirMetric("Carbon Monoxide") },
                modifier = Modifier.weight(1f)
            )
            AirCard(
                value = o3Value,
                unit = o3Unit,
                label = "O₃",
                isEnabled = "O₃" !in hiddenAirMetrics,
                onPlusClick = { onToggleAirMetric("O₃") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AirCard(
                value = pm25Value,
                unit = pm25Unit,
                label = "PM2.5",
                isEnabled = "PM2.5" !in hiddenAirMetrics,
                onPlusClick = { onToggleAirMetric("PM2.5") },
                modifier = Modifier.weight(1f)
            )
            AirCard(
                value = pm10Value,
                unit = pm10Unit,
                label = "PM10",
                isEnabled = "PM10" !in hiddenAirMetrics,
                onPlusClick = { onToggleAirMetric("PM10") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AirCard(
    value: String,
    unit: String,
    label: String,
    isEnabled: Boolean,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = if (isEnabled) Color(0xFF404040) else Color(0xFFB0B0B0)
    val valueColor = if (isEnabled) Color(0xFF404040) else Color(0xFFB0B0B0)

    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) {
                Color.White.copy(alpha = 0.9f)
            } else {
                Color(0xFFE0E0E0)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onPlusClick,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    val toggleIcon = if (isEnabled) Icons.Default.Remove else Icons.Default.Add
                    val toggleDescription = if (isEnabled) "Hide metric" else "Show metric"

                    Icon(
                        imageVector = toggleIcon,
                        contentDescription = toggleDescription,
                        tint = if (isEnabled) Color(0xFF808080) else Color(0xFFB0B0B0)
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    color = valueColor
                )
            }

            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}


// Pollen layout
@Composable
private fun PollenAdjustments(
    hiddenPollenMetrics: Set<String>,
    onTogglePollenType: (String) -> Unit,
    pollenData: PollenScreenData
) {
    val byName = pollenData.typeCards.associateBy { it.name }

    fun extractNumber(raw: String): Int {
        val digits = raw.takeWhile { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    val oakValue = extractNumber(byName["Oak"]?.value ?: "370 ppm")
    val weedsValue = extractNumber(byName["Weeds"]?.value ?: "20 ppm")
    val treesValue = extractNumber(byName["Trees"]?.value ?: "220 ppm")
    val grassValue = extractNumber(byName["Grass"]?.value ?: "60 ppm")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenCard(
                name = "Oak",
                value = oakValue,
                max = 400,
                ringColor = Color(0xFFE53935),
                isEnabled = "Oak" !in hiddenPollenMetrics,
                onPlusClick = { onTogglePollenType("Oak") },
                modifier = Modifier.weight(1f)
            )
            PollenCard(
                name = "Weeds",
                value = weedsValue,
                max = 100,
                ringColor = Color(0xFF43A047),
                isEnabled = "Weeds" !in hiddenPollenMetrics,
                onPlusClick = { onTogglePollenType("Weeds") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenCard(
                name = "Trees",
                value = treesValue,
                max = 400,
                ringColor = Color(0xFFFDD835),
                isEnabled = "Trees" !in hiddenPollenMetrics,
                onPlusClick = { onTogglePollenType("Trees") },
                modifier = Modifier.weight(1f)
            )
            PollenCard(
                name = "Grass",
                value = grassValue,
                max = 100,
                ringColor = Color(0xFF43A047),
                isEnabled = "Grass" !in hiddenPollenMetrics,
                onPlusClick = { onTogglePollenType("Grass") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun PollenCard(
    name: String,
    value: Int,
    max: Int,
    ringColor: Color,
    isEnabled: Boolean,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val effectiveRingColor = if (isEnabled) ringColor else Color(0xFFB0B0B0)
    val textColor = if (isEnabled) Color(0xFF404040) else Color(0xFFB0B0B0)

    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) {
                Color.White.copy(alpha = 0.9f)
            } else {
                Color(0xFFE0E0E0)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onPlusClick,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    val toggleIcon = if (isEnabled) Icons.Default.Remove else Icons.Default.Add
                    val toggleDescription = if (isEnabled) "Hide metric" else "Show metric"

                    Icon(
                        imageVector = toggleIcon,
                        contentDescription = toggleDescription,
                        tint = if (isEnabled) Color(0xFF808080) else Color(0xFFB0B0B0)
                    )
                }
            }

            val progress = (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(72.dp),
                    color = effectiveRingColor,
                    strokeWidth = 8.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$value ppm",
                            fontSize = 11.sp,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

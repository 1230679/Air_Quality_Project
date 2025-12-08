package com.example.livelifebreatheair.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
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

enum class AdjustmentTab { AIR, POLLEN }

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {}
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
                        text = "Profil",
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
                    AdjustmentTab.AIR -> AirAdjustments()
                    AdjustmentTab.POLLEN -> PollenAdjustments()
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ---------- Top menu items ---------- */

@Composable
private fun ProfileMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.ArrowForward
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

/* ---------- Segmented control ---------- */

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

/* ---------- Air layout ---------- */

@Composable
private fun AirAdjustments() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AirCard(
                value = "10",
                unit = "mg/m³",
                label = "Carbon Monoxide",
                modifier = Modifier.weight(1f)
            )
            AirCard(
                value = "100",
                unit = "µg/m³",
                label = "O₃",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AirCard(
                value = "12",
                unit = "µg/m³",
                label = "PM2.5",
                modifier = Modifier.weight(1f)
            )
            AirCard(
                value = "8",
                unit = "µg/m³",
                label = "PM10",
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // +/- knoppen
            Row(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* TODO: value-- */ },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = Color(0xFF808080)
                    )
                }
                Spacer(Modifier.width(40.dp))
                IconButton(
                    onClick = { /* TODO: value++ */ },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color(0xFF808080)
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF404040)
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF808080)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF808080),
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

/* ---------- Pollen layout ---------- */

@Composable
private fun PollenAdjustments() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenCard(
                name = "Oak",
                value = 370,
                max = 400,
                ringColor = Color(0xFFE53935),
                modifier = Modifier.weight(1f)
            )
            PollenCard(
                name = "Weeds",
                value = 20,
                max = 100,
                ringColor = Color(0xFF43A047),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PollenCard(
                name = "Trees",
                value = 220,
                max = 400,
                ringColor = Color(0xFFFDD835),
                modifier = Modifier.weight(1f)
            )
            PollenCard(
                name = "Grass",
                value = 60,
                max = 100,
                ringColor = Color(0xFF43A047),
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // +/- knoppen
            Row(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* minder */ },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = Color(0xFF808080)
                    )
                }
                Spacer(Modifier.width(40.dp))
                IconButton(
                    onClick = { /* meer */ },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color(0xFF808080)
                    )
                }
            }

            // Cirkel (progress)
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val progress = (value.toFloat() / max.toFloat()).coerceIn(0f, 1f)

                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(72.dp),
                        strokeWidth = 8.dp,
                        color = ringColor,
                        trackColor = Color(0xFFE0E0E0)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            color = Color(0xFF404040),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$value ppm",
                            fontSize = 11.sp,
                            color = Color(0xFF808080)
                        )
                    }
                }
            }
        }
    }
}

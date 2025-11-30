package com.example.livelifebreatheair.ui.components

import androidx.compose.foundation.layout.fillMaxSize

//package com.example.livelifebreatheair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NavigationTab {
    AIR_QUALITY,
    WEATHER,
    POLLEN,
    HISTORY
}

data class TabItem(
    val tab: NavigationTab,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomTabNavigation(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    val tabs = listOf(
        TabItem(NavigationTab.AIR_QUALITY, "Air quality", Icons.Outlined.Air),
        TabItem(NavigationTab.WEATHER, "Weather", Icons.Outlined.WbSunny),
        TabItem(NavigationTab.POLLEN, "Pollen", Icons.Outlined.AcUnit),
        TabItem(NavigationTab.HISTORY, "Historie", Icons.Outlined.History)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(cs.surfaceVariant),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tabItem ->
                TabButton(
                    tabItem = tabItem,
                    isSelected = selectedTab == tabItem.tab,
                    onClick = { onTabSelected(tabItem.tab) }
                )
            }
        }
    }
}

@Composable
private fun TabButton(
    tabItem: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (isSelected) cs.surface else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = tabItem.icon,
                contentDescription = tabItem.label,
                modifier = Modifier.size(20.dp),
                tint = if (isSelected) cs.onSurface else cs.onSurfaceVariant
            )

            if (isSelected) {
                Text(
                    text = tabItem.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = cs.onSurface
                )
            }
        }
    }
}

// Beispiel: Integration in MainActivity
@Composable
fun MainScreenWithTabs(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()) {
        // Dein Hauptinhalt basierend auf selectedTab
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = cs.background
        ) {
            when (currentTab) {
                NavigationTab.AIR_QUALITY -> AirQualityContent()
                NavigationTab.WEATHER -> WeatherContent()
                NavigationTab.POLLEN -> PollenContent()
                NavigationTab.HISTORY -> HistoryContent()
            }
        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomTabNavigation(
                selectedTab = currentTab,
                onTabSelected = onTabSelected
            )
        }
    }
}

// Placeholder Content Screens
@Composable
fun AirQualityContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Air Quality Screen")
    }
}

@Composable
fun WeatherContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Weather Screen")
    }
}

@Composable
fun PollenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Pollen Screen")
    }
}

@Composable
fun HistoryContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("History Screen")
    }
}
package com.example.livelifebreatheair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.livelifebreatheair.ui.theme.LiveLifeBreatheAirTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
// import androidx.compose.material.icons.filled.Minus
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveLifeBreatheAirTheme {
                MainScreen()
                }
            }
        }
}

@Composable
fun MainScreen() {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = cs.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Welcome\nusername!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f)
                )
                // profile picture placeholder
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(cs.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) { /* empty placeholder */ }
            }

            SettingsRow("Personal data")
            SettingsRow("Notifications")
            SettingsRow("Settings")

            Text(
                text = "Adjustments",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            SegmentedTabs()

            MetricsGrid()
        }
    }
}

@Composable
private fun SettingsRow(title: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun SegmentedTabs() {
    var selected by remember { mutableIntStateOf(0) }
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(cs.secondaryContainer)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Segment(selected == 0, { selected = 0 }, Modifier.weight(1f)) {
            Icon(Icons.Outlined.Air, contentDescription = "Air")
        }
        Spacer(Modifier.width(6.dp))
        Segment(selected == 1, { selected = 1 }, Modifier.weight(1f)) {
            Icon(Icons.Outlined.LocalFlorist, contentDescription = "Pollen")
        }
    }
}

@Composable
private fun Segment(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier.fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
private fun MetricsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard(label = "temperature", valueText = "21°C")
            MetricCard(label = "humidity", valueText = "80%")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard(label = "UV-index", valueText = "2")
            EmptyTile()
        }
    }
}

@Composable
private fun MetricCard(label: String, valueText: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            //.weight(1f)
            .height(110.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            // first line needs to be changed to minus symbol
            SmallIconButton(Icons.Default.Add, Modifier.align(Alignment.TopStart).padding(8.dp))
            SmallIconButton(Icons.Default.Add, Modifier.align(Alignment.TopEnd).padding(8.dp))

            Column(
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(valueText, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold))
                Spacer(Modifier.height(6.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun EmptyTile() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            //.weight(1f)
            .height(110.dp)
    ) { /* intentionally blank */ }
}

@Composable
private fun SmallIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = CircleShape,
        modifier = modifier.size(28.dp),
        onClick = { /* hook up later */ }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null)
        }
    }
}
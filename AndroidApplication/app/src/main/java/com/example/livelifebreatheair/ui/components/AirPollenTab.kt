package com.example.livelifebreatheair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.livelifebreatheair.ui.screens.AppScreen

@Composable
fun AirPollenTab(
    selected: AppScreen,
    onSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(cs.secondaryContainer)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Segment(
            selected = selected == AppScreen.Air,
            onClick = { onSelected(AppScreen.Air) },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.Air, contentDescription = "Air")
        }
        Spacer(Modifier.width(6.dp))
        Segment(
            selected = selected == AppScreen.Weather,
            onClick = { onSelected(AppScreen.Weather) },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.WbSunny, contentDescription = "Weather")
        }
        Spacer(Modifier.width(6.dp))
        Segment(
            selected = selected == AppScreen.Pollen,
            onClick = { onSelected(AppScreen.Pollen) },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.LocalFlorist, contentDescription = "Pollen")
        }
        Spacer(Modifier.width(6.dp))
        Segment(
            selected = selected == AppScreen.History,
            onClick = { onSelected(AppScreen.History) },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.History, contentDescription = "History")
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

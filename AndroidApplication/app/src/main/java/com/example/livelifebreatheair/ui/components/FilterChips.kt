package com.example.livelifebreatheair.ui.components

//package com.example.livelifebreatheair.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class DataCategory {
    ALL,
    AIR_QUALITY,
    WEATHER,
    POLLEN
}

data class ChipItem(
    val category: DataCategory,
    val label: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChips(
    selectedCategories: Set<DataCategory>,
    onCategoryToggle: (DataCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = listOf(
        ChipItem(DataCategory.ALL, "All"),
        ChipItem(DataCategory.AIR_QUALITY, "Air Quality"),
        ChipItem(DataCategory.WEATHER, "Weather"),
        ChipItem(DataCategory.POLLEN, "Pollen")
    )

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            FilterChip(
                selected = selectedCategories.contains(chip.category),
                onClick = { onCategoryToggle(chip.category) },
                label = { Text(chip.label) },
                shape = RoundedCornerShape(50.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

// Data for Charts
data class ChartData(
    val id: String,
    val title: String,
    val category: DataCategory,
    val values: List<Int>,
    val labels: List<String> = emptyList(),  // ["Mon", "Tue", "Wed"]
    val unit: String = ""  // "µg/m³", "°C", "ppm"
)
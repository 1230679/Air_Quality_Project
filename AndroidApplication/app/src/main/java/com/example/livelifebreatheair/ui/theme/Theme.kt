package com.example.livelifebreatheair.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CardGrey,
    onPrimary = TextOnLight,
    primaryContainer = CardGrey,
    onPrimaryContainer = TextOnLight,
    secondaryContainer = CardLightGrey,
    onSecondaryContainer = TextOnLight,
    background = DarkBackground,
    onBackground = TextOnDark,
    surface = PanelBackground,
    onSurface = TextOnLight,
)


private val LightColorScheme = lightColorScheme(
    primary = CardYellow,
    onPrimary = TextOnYellow,
    primaryContainer = CardYellow,          // metric cards etc.
    onPrimaryContainer = TextOnYellow,
    secondary = PillYellow,
    onSecondary = TextOnYellow,
    secondaryContainer = PillYellow,        // settings rows + segmented background
    onSecondaryContainer = TextOnYellow,
    background = CreamBackground,           // whole screen
    onBackground = TextOnBg,
    surface = CreamBackground,
    onSurface = TextOnBg,
    outline = OutlineYellow,
)

@Composable
fun LiveLifeBreatheAirTheme(
    darkTheme: Boolean = false,            // lock to light for now
    dynamicColor: Boolean = false,         // IMPORTANT: don’t use system dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
        // if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

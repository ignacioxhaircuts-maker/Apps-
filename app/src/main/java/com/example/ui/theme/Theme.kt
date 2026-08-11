package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = Color.Black,
    primaryContainer = OrangeDark,
    onPrimaryContainer = Color.White,
    secondary = OrangeSecondary,
    onSecondary = Color.White,
    tertiary = YellowAccent,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeSecondary,
    onPrimary = Color.White,
    primaryContainer = YellowPrimary,
    onPrimaryContainer = Color.Black,
    secondary = YellowAccent,
    onSecondary = Color.Black,
    tertiary = OrangeDark,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight
)

@Composable
fun YellowCartoonTheme(
    darkTheme: Boolean = true, // Default to sleek dark TV mode with gradient yellow & orange accents
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

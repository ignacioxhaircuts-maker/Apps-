package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val YellowPrimary = Color(0xFFFFD600)
val OrangeSecondary = Color(0xFFFF6D00)
val YellowAccent = Color(0xFFFFAB00)
val OrangeDark = Color(0xFFE65100)

val DarkBackground = Color(0xFF101014)
val DarkSurface = Color(0xFF1B1B22)
val DarkCardSurface = Color(0xFF252530)

val LightBackground = Color(0xFFFFFDE7)
val LightSurface = Color(0xFFFFF8E1)

val TextPrimaryDark = Color(0xFFFFFFFF)
val TextSecondaryDark = Color(0xFFB0BEC5)
val TextPrimaryLight = Color(0xFF1A1A1A)

val YellowOrangeGradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFEA00), // Electric Yellow
        Color(0xFFFFB300), // Amber Yellow
        Color(0xFFFF8F00), // Bright Orange
        Color(0xFFFF6D00), // Deep Orange
        Color(0xFFDD2C00)  // Sunset Red-Orange
    )
)

val YellowOrangeDarkGradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2B2000), // Deep Golden Yellow Glow
        Color(0xFF211502), // Dark Amber
        Color(0xFF171113), // Charcoal Sunset
        Color(0xFF101014)  // Dark Background
    )
)


package com.kashif.kmmnewsapp.android.design

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Colors {
    // Primary Colors - iOS inspired
    val Primary = Color(0xFF007AFF) // iOS Blue
    val Secondary = Color(0xFF5856D6) // iOS Purple
    val Success = Color(0xFF34C759) // iOS Green
    val Warning = Color(0xFFFF9500) // iOS Orange
    val Error = Color(0xFFFF3B30) // iOS Red
    
    // Light Theme
    val LightBackground = Color(0xFFF2F2F7) // iOS Light Gray
    val LightSurface = Color(0xFFFFFFFF)
    val LightText = Color(0xFF000000)
    val LightTextSecondary = Color(0xFF6C6C70)
    val LightDivider = Color(0xFFC6C6C8)
    
    // Dark Theme
    val DarkBackground = Color(0xFF000000)
    val DarkSurface = Color(0xFF1C1C1E)
    val DarkText = Color(0xFFFFFFFF)
    val DarkTextSecondary = Color(0xFF8E8E93)
    val DarkDivider = Color(0xFF38383A)

    val LightColorScheme = lightColorScheme(
        primary = Primary,
        secondary = Secondary,
        background = LightBackground,
        surface = LightSurface,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = LightText,
        onSurface = LightText
    )

    val DarkColorScheme = darkColorScheme(
        primary = Primary,
        secondary = Secondary,
        background = DarkBackground,
        surface = DarkSurface,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = DarkText,
        onSurface = DarkText
    )
} 
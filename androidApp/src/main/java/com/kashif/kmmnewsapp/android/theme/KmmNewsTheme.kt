package com.kashif.kmmnewsapp.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green

private val LightColorPalette = lightColors(
    primary = GreenSecondary,
    primaryVariant = YellowGreen,
    secondary = Green,
    secondaryVariant = YellowGreenSecondary,
    background = Background,
    surface = Background,
    onPrimary = Color.White,
    onSecondary = OnPrimary,
    onBackground = DarkBlue,
    onSurface = DarkBlue,
    error = Danger,
    onError = OnPrimary
)

@Composable
fun KmmNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {


    val colors = if (darkTheme) {
        LightColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = NunitoTypography,
        shapes = AppShapes,
        content = content
    )
}
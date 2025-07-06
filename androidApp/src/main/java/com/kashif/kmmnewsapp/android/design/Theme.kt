package com.kashif.kmmnewsapp.android.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

@Composable
fun KmmNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) Colors.DarkColorScheme else Colors.LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

val MaterialTheme.elevation: Elevation
    @ReadOnlyComposable
    @Composable
    get() = LocalElevation.current

val LocalElevation = androidx.compose.runtime.staticCompositionLocalOf { Elevation() }

class Elevation {
    val Level0 = 0.dp
    val Level1 = 2.dp
    val Level2 = 4.dp
    val Level3 = 8.dp
    val Level4 = 16.dp
    val Level5 = 24.dp
    val Level6 = 32.dp
}
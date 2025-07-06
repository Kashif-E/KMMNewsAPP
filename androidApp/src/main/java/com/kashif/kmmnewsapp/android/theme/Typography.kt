package com.kashif.kmmnewsapp.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kashif.kmmnewsapp.android.R

private val SFPro = FontFamily(
    Font(R.font.regular, FontWeight.Normal),
    Font(R.font.mediym, FontWeight.Medium),
    Font(R.font.light, FontWeight.Light)
)

val AppTypography = Typography(
    // Large Title - iOS style
    displayLarge = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.37.sp
    ),
    
    // Title 1
    displayMedium = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.36.sp
    ),
    
    // Title 2
    displaySmall = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.35.sp
    ),
    
    // Title 3
    headlineLarge = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.38.sp
    ),
    
    // Headline
    headlineMedium = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    
    // Body
    bodyLarge = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    
    // Callout
    bodyMedium = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.32).sp
    ),
    
    // Subheadline
    bodySmall = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.24).sp
    ),
    
    // Footnote
    labelLarge = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.08).sp
    ),
    
    // Caption 1
    labelMedium = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    
    // Caption 2
    labelSmall = TextStyle(
        fontFamily = SFPro,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.06.sp
    )
) 
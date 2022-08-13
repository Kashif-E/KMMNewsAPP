package com.kashif.kmmnewsapp.android.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kashif.kmmnewsapp.android.R


val Popins = FontFamily(
    Font(R.font.light),
    Font(R.font.regular),
    Font(R.font.mediym)
)

val NunitoTypography = Typography(
    h1 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),
    h2 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    h4 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    h5 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    h6 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 14.sp
    ),
    body2 = TextStyle(
        fontFamily = Popins,
        fontSize = 10.sp
    ),
    button = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        color = OnPrimary
    ),
    caption = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp
    ),
    overline = TextStyle(
        fontFamily = Popins,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)
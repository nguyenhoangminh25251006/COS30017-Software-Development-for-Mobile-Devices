package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Material typography; only bodyLarge is overridden, the rest use Material 3 defaults
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Named text styles reused across both activities, so headings, badges and captions
 * look the same on the catalogue and rental screens. Colour is not part of the style,
 * so each style adapts to light and dark mode through the colour scheme.
 */
object AppTextStyles {
    /** Section headings: "Popular", "Favourites (n)", "Active Rentals (n)", "Rental Duration". */
    val SectionTitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.3).sp
    )

    /** Text inside small rounded badges: "5 cars ready", the year badge, the day counter. */
    val BadgeLabel = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )

    /** Small secondary captions: car specs, slider limits, balance and cost labels. */
    val Caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
}

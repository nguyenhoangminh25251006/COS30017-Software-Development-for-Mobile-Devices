package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CarPrimaryDark,
    onPrimary = CarOnPrimaryDark,
    primaryContainer = CarPrimaryContainerDark,
    onPrimaryContainer = CarOnPrimaryContainerDark,
    secondary = CarSecondaryDark,
    onSecondary = CarOnSecondaryDark,
    secondaryContainer = CarSecondaryContainerDark,
    onSecondaryContainer = CarOnSecondaryContainerDark,
    background = CarBackgroundDark,
    onBackground = CarOnBackgroundDark,
    surface = CarSurfaceDark,
    onSurface = CarOnSurfaceDark,
    surfaceVariant = CarSurfaceVariantDark,
    onSurfaceVariant = CarOnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = CarPrimaryLight,
    onPrimary = CarOnPrimaryLight,
    primaryContainer = CarPrimaryContainerLight,
    onPrimaryContainer = CarOnPrimaryContainerLight,
    secondary = CarSecondaryLight,
    onSecondary = CarOnSecondaryLight,
    secondaryContainer = CarSecondaryContainerLight,
    onSecondaryContainer = CarOnSecondaryContainerLight,
    background = CarBackgroundLight,
    onBackground = CarOnBackgroundLight,
    surface = CarSurfaceLight,
    onSurface = CarOnSurfaceLight,
    surfaceVariant = CarSurfaceVariantLight,
    onSurfaceVariant = CarOnSurfaceVariantLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.myapplication.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Provides locale override for Jetpack Compose subtrees, enabling instant in-app
 * language switching between Vietnamese ("vi") and English ("en").
 */
@Composable
fun AppLocaleProvider(
    language: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val currentConfig = LocalConfiguration.current
    val locale = remember(language) { Locale.forLanguageTag(language) }

    val configuration = remember(locale, currentConfig) {
        val config = Configuration(currentConfig)
        config.setLocale(locale)
        config
    }

    val localizedContext = remember(context, locale) {
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }

    CompositionLocalProvider(
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        content()
    }
}

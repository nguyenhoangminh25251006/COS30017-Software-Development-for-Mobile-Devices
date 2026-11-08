package com.example.myapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Thin 1dp outline used by cards, badges and outlined buttons across both screens.
 */
@Composable
fun outlineBorder(): BorderStroke =
    BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

/**
 * Bold section heading (e.g. "Popular", "Favourites (2)").
 */
@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            letterSpacing = (-0.3).sp
        ),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

/**
 * Small rounded outlined badge with neutral background (e.g. "5 cars ready", "1 / 5", "2024").
 */
@Composable
fun OutlinedBadge(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = outlineBorder(),
        modifier = modifier,
        content = content
    )
}

package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * Reusable Star Rating Bar component for displaying vehicle ratings from 1 to 5.
 * Satisfies the requirement for non-TextView widgets (RatingBar widget equivalent).
 */
@Composable
fun StarRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 22.dp,
    starColor: Color = Color(0xFFFFB300), // Gold / Amber
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val icon = when {
                rating >= i -> Icons.Filled.Star
                rating >= i - 0.5f -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = starColor,
                modifier = Modifier
                    .size(starSize)
                    .padding(end = 2.dp)
            )
        }
        if (showText) {
            Text(
                text = String.format(Locale.US, "%.1f", rating),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

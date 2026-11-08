package com.example.myapplication.model

import androidx.annotation.StringRes
import com.example.myapplication.R

/**
 * Enumeration representing sorting options for the car catalogue.
 * [labelRes] is the localized label shown on the sort chip.
 */
enum class SortOption(@StringRes val labelRes: Int) {
    RATING_DESC(R.string.sort_rating),
    YEAR_DESC(R.string.sort_newest),
    COST_ASC(R.string.sort_lowest_price)
}

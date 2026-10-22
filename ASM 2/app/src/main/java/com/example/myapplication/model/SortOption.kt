package com.example.myapplication.model

/**
 * Enumeration representing sorting options for the car catalogue.
 */
enum class SortOption(val label: String) {
    RATING_DESC("Rating: Highest to Lowest"),
    YEAR_DESC("Year: Newest to Oldest"),
    COST_ASC("Daily Cost: Lowest to Highest")
}

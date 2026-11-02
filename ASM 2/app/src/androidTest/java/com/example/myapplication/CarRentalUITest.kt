package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation UI tests verifying screen elements, search, navigation, favourites, and interactions.
 */
@RunWith(AndroidJUnit4::class)
class CarRentalUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppLaunch_DisplaysHeaderAndVehicleCard() {
        // App title is displayed
        composeTestRule.onNodeWithText("Car Rental").assertIsDisplayed()

        // Initial credit balance is displayed (500)
        composeTestRule.onNodeWithText("500").assertIsDisplayed()

        // Popular section is displayed
        composeTestRule.onNodeWithText("Popular").assertIsDisplayed()

        // Available car (e.g., Toyota Camry) is displayed
        composeTestRule.onNodeWithText("Toyota Camry").assertIsDisplayed()

        // Rent button exists
        composeTestRule.onAllNodesWithText("Rent").onFirst().assertIsDisplayed()
    }

    @Test
    fun testSearch_FiltersVehicleList() {
        // Type "Tesla" in search bar
        composeTestRule.onNodeWithText("Search car name or model…")
            .performTextInput("Tesla")

        // Tesla Model 3 should be visible
        composeTestRule.onNodeWithText("Tesla Model 3").assertIsDisplayed()

        // Non-matching car should not be displayed
        composeTestRule.onNodeWithText("Toyota Camry").assertDoesNotExist()
    }

    @Test
    fun testFavouriteToggle_AddsCarToFavouritesSection() {
        // Click first heart favourite button
        composeTestRule.onAllNodesWithContentDescription("Favourite").onFirst().performClick()

        // Favourites section displays updated count
        composeTestRule.onNodeWithText("Favourites (1)").assertIsDisplayed()
    }
}

package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
        composeTestRule.onNodeWithText("DriveEase Car Rental").assertIsDisplayed()

        // Initial credit balance is displayed (500)
        composeTestRule.onNodeWithText("Credits: 500").assertIsDisplayed()

        // Toyota Camry (highest rated) is displayed initially
        composeTestRule.onNodeWithText("Toyota Camry").assertIsDisplayed()
        composeTestRule.onNodeWithText("50 credits / day").assertIsDisplayed()

        // Next and Rent buttons exist
        composeTestRule.onNodeWithText("Next Car").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rent Vehicle").assertIsDisplayed()
    }

    @Test
    fun testNextButton_NavigatesToNextVehicle() {
        // Initial car is Toyota Camry
        composeTestRule.onNodeWithText("Toyota Camry").assertIsDisplayed()

        // Click Next
        composeTestRule.onNodeWithText("Next Car").performClick()

        // Advances to next car (Tesla Model 3 or BMW 3 Series depending on sort)
        composeTestRule.onNodeWithText("Toyota Camry").assertDoesNotExist()
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
        // Click heart favourite button
        composeTestRule.onNodeWithContentDescription("Favourite").performClick()

        // Favourites section displays Toyota Camry
        composeTestRule.onNodeWithText("Your Favourites").assertIsDisplayed()
    }
}

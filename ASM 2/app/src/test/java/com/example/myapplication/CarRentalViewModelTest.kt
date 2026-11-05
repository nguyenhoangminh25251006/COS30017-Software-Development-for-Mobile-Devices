package com.example.myapplication

import com.example.myapplication.ui.CarRentalViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CarRentalViewModel], including language switching and toast messaging.
 */
class CarRentalViewModelTest {

    private lateinit var viewModel: CarRentalViewModel

    @Before
    fun setUp() {
        viewModel = CarRentalViewModel()
    }

    @Test
    fun testDefaultLanguageIsEnglish() {
        assertEquals("en", viewModel.appLanguage.value)
    }

    @Test
    fun testSwitchToVietnamese() {
        viewModel.setAppLanguage("vi")
        assertEquals("vi", viewModel.appLanguage.value)
    }

    @Test
    fun testToggleLanguage() {
        assertEquals("en", viewModel.appLanguage.value)
        viewModel.toggleAppLanguage()
        assertEquals("vi", viewModel.appLanguage.value)
        viewModel.toggleAppLanguage()
        assertEquals("en", viewModel.appLanguage.value)
    }

    @Test
    fun testIgnoreInvalidLanguageCode() {
        viewModel.setAppLanguage("fr")
        assertEquals("en", viewModel.appLanguage.value)
    }
}

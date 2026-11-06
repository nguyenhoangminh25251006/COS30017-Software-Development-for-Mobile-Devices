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

    @Test
    fun testSetCurrentCarIndexPersistsState() {
        assertEquals(0, viewModel.currentCarIndex.value)
        viewModel.setCurrentCarIndex(2)
        assertEquals(2, viewModel.currentCarIndex.value)
        val car = viewModel.currentCar.value
        assertEquals(viewModel.availableCars.value[2].id, car?.id)
    }

    @Test
    fun testSetCurrentCarIndexCoercesBounds() {
        viewModel.setCurrentCarIndex(999)
        val maxIndex = viewModel.availableCars.value.size - 1
        assertEquals(maxIndex, viewModel.currentCarIndex.value)

        viewModel.setCurrentCarIndex(-5)
        assertEquals(0, viewModel.currentCarIndex.value)
    }
}

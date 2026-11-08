package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.CarRentalRepository
import com.example.myapplication.domain.CarRentalDomain
import com.example.myapplication.domain.ValidationResult
import com.example.myapplication.model.Car
import com.example.myapplication.model.RentalBooking
import com.example.myapplication.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Message descriptor for localized Toast notifications.
 */
data class LocalizedToastMessage(
    val resId: Int,
    val formatArgs: List<Any> = emptyList()
)

/**
 * Shared ViewModel managing application UI state and user interactions.
 */
class CarRentalViewModel(
    private val repository: CarRentalRepository = CarRentalRepository()
) : ViewModel() {

    val creditBalance: StateFlow<Int> = repository.creditBalance
    val favourites: StateFlow<Set<String>> = repository.favourites
    val favouriteCars: StateFlow<List<Car>> = repository.favouriteCars
    val rentedBookings: StateFlow<List<RentalBooking>> = repository.rentedBookings
    val searchQuery: StateFlow<String> = repository.searchQuery
    val sortOption: StateFlow<SortOption> = repository.sortOption
    val currentCarIndex: StateFlow<Int> = repository.currentCarIndex
    val availableCars: StateFlow<List<Car>> = repository.availableCars

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _appLanguage = MutableStateFlow("en")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    private val _localizedToast = MutableStateFlow<LocalizedToastMessage?>(null)
    val localizedToast: StateFlow<LocalizedToastMessage?> = _localizedToast.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setAppLanguage(lang: String) {
        if (lang == "en" || lang == "vi") {
            _appLanguage.value = lang
        }
    }

    fun toggleAppLanguage() {
        _appLanguage.value = if (_appLanguage.value == "vi") "en" else "vi"
    }

    fun setSearchQuery(query: String) = repository.setSearchQuery(query)

    fun setSortOption(option: SortOption) = repository.setSortOption(option)

    fun toggleFavourite(carId: String) = repository.toggleFavourite(carId)

    fun setCurrentCarIndex(index: Int) = repository.setCurrentCarIndex(index)

    fun processBookingResult(carId: String, rentalDays: Int) {
        val car = repository.sampleCars.find { it.id == carId } ?: return
        if (repository.processBooking(car, rentalDays) is ValidationResult.Success) {
            val totalCost = CarRentalDomain.calculateTotalCost(car.dailyCost, rentalDays)
            _localizedToast.value = LocalizedToastMessage(
                resId = R.string.booking_successful,
                formatArgs = listOf(totalCost, rentalDays)
            )
        }
    }

    fun returnRentedCar(carId: String) {
        val booking = repository.returnRentedCar(carId) ?: return
        _localizedToast.value = LocalizedToastMessage(
            resId = R.string.vehicle_returned,
            formatArgs = listOf(booking.car.name)
        )
    }

    fun notifyBookingCancelled() {
        _localizedToast.value = LocalizedToastMessage(resId = R.string.booking_cancelled)
    }

    fun clearToast() {
        _localizedToast.value = null
    }
}

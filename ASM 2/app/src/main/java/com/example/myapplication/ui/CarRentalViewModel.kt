package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.CarRentalRepository
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
    val repository: CarRentalRepository = CarRentalRepository()
) : ViewModel() {

    val creditBalance: StateFlow<Int> = repository.creditBalance
    val favourites: StateFlow<Set<String>> = repository.favourites
    val favouriteCars: StateFlow<List<Car>> = repository.favouriteCars
    val rentedBookings: StateFlow<List<RentalBooking>> = repository.rentedBookings
    val searchQuery: StateFlow<String> = repository.searchQuery
    val sortOption: StateFlow<SortOption> = repository.sortOption
    val currentCarIndex: StateFlow<Int> = repository.currentCarIndex
    val availableCars: StateFlow<List<Car>> = repository.availableCars
    val currentCar: StateFlow<Car?> = repository.currentCar

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _appLanguage = MutableStateFlow("en")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

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

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun setSortOption(option: SortOption) {
        repository.setSortOption(option)
    }

    fun toggleFavourite(carId: String) {
        repository.toggleFavourite(carId)
    }

    fun isFavourite(carId: String): Boolean {
        return repository.isFavourite(carId)
    }

    fun nextCar() {
        repository.nextCar()
    }

    fun previousCar() {
        repository.previousCar()
    }

    fun getAvailableCars(): List<Car> {
        return repository.getAvailableCars()
    }

    fun getCurrentCar(): Car? {
        return repository.getCurrentCar()
    }

    fun getFavouriteCars(): List<Car> {
        return repository.getFavouriteCars()
    }

    fun processBookingResult(carId: String, rentalDays: Int): ValidationResult {
        val car = repository.sampleCars.find { it.id == carId }
            ?: return ValidationResult.Error("Vehicle not found.")
        val result = repository.processBooking(car, rentalDays)
        if (result is ValidationResult.Success) {
            val totalCost = car.dailyCost * rentalDays
            _userMessage.value = "Booking confirmed for ${car.name}! $totalCost credits deducted."
            _localizedToast.value = LocalizedToastMessage(
                resId = R.string.booking_successful,
                formatArgs = listOf(totalCost, rentalDays)
            )
        }
        return result
    }

    fun returnRentedCar(carId: String) {
        val car = repository.sampleCars.find { it.id == carId }
        val success = repository.returnRentedCar(carId)
        if (success) {
            val carName = car?.name ?: "Vehicle"
            _userMessage.value = "$carName returned to catalogue. Credits refunded!"
            _localizedToast.value = LocalizedToastMessage(
                resId = R.string.vehicle_returned,
                formatArgs = listOf(carName)
            )
        }
    }

    fun notifyBookingCancelled() {
        _userMessage.value = "Booking cancelled. No credits deducted."
        _localizedToast.value = LocalizedToastMessage(
            resId = R.string.booking_cancelled
        )
    }

    fun clearUserMessage() {
        _userMessage.value = null
        _localizedToast.value = null
    }
}

package com.example.myapplication.data

import com.example.myapplication.R
import com.example.myapplication.domain.CarRentalDomain
import com.example.myapplication.domain.ValidationResult
import com.example.myapplication.model.Car
import com.example.myapplication.model.RentalBooking
import com.example.myapplication.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory repository for application data.
 * Manages the car dataset, user credits, favourites, active bookings, search, and sorting.
 * No persistent storage (database/preferences) is used per specification.
 */
class CarRentalRepository {

    // 5 distinct sample cars with realistic, internally consistent data
    val sampleCars = listOf(
        Car(
            id = "car_001",
            name = "Toyota Camry",
            model = "Camry SE",
            year = 2022,
            rating = 4.8f,
            kmsDriven = 24500,
            dailyCost = 50,
            imageResId = R.drawable.car_toyota_camry
        ),
        Car(
            id = "car_002",
            name = "Tesla Model 3",
            model = "Long Range Dual Motor",
            year = 2023,
            rating = 4.9f,
            kmsDriven = 12000,
            dailyCost = 75,
            imageResId = R.drawable.car_tesla_model3
        ),
        Car(
            id = "car_003",
            name = "Hyundai Tucson",
            model = "Tucson SEL",
            year = 2021,
            rating = 4.5f,
            kmsDriven = 38000,
            dailyCost = 45,
            imageResId = R.drawable.car_hyundai_tucson
        ),
        Car(
            id = "car_004",
            name = "BMW 3 Series",
            model = "330i xDrive",
            year = 2024,
            rating = 4.7f,
            kmsDriven = 8500,
            dailyCost = 90,
            imageResId = R.drawable.car_bmw_3series
        ),
        Car(
            id = "car_005",
            name = "Honda Civic",
            model = "Civic EX",
            year = 2020,
            rating = 4.3f,
            kmsDriven = 52000,
            dailyCost = 35,
            imageResId = R.drawable.car_honda_civic
        )
    )

    private val _canonicalCars = sampleCars.toList()

    private val _creditBalance = MutableStateFlow(CarRentalDomain.INITIAL_CREDIT_BALANCE)
    val creditBalance: StateFlow<Int> = _creditBalance.asStateFlow()

    private val _favourites = MutableStateFlow<Set<String>>(emptySet())
    val favourites: StateFlow<Set<String>> = _favourites.asStateFlow()

    private val _rentedBookings = MutableStateFlow<List<RentalBooking>>(emptyList())
    val rentedBookings: StateFlow<List<RentalBooking>> = _rentedBookings.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.RATING_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _currentCarIndex = MutableStateFlow(0)
    val currentCarIndex: StateFlow<Int> = _currentCarIndex.asStateFlow()

    /**
     * Gets the list of available cars (excluding rented cars), filtered by search query and sorted.
     */
    fun getAvailableCars(): List<Car> {
        val rentedIds = _rentedBookings.value.map { it.car.id }.toSet()
        return CarRentalDomain.getAvailableCars(
            allCars = _canonicalCars,
            rentedCarIds = rentedIds,
            searchQuery = _searchQuery.value,
            sortOption = _sortOption.value
        )
    }

    /**
     * Gets the currently displayed car, or null if no cars are available.
     */
    fun getCurrentCar(): Car? {
        val available = getAvailableCars()
        if (available.isEmpty()) return null
        val index = _currentCarIndex.value.coerceIn(0, available.size - 1)
        return available[index]
    }

    /**
     * Advances to the next available car in the list.
     */
    fun nextCar() {
        val available = getAvailableCars()
        if (available.isEmpty()) {
            _currentCarIndex.value = 0
            return
        }
        val nextIndex = (_currentCarIndex.value + 1) % available.size
        _currentCarIndex.value = nextIndex
    }

    /**
     * Updates search query and resets car navigation index.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _currentCarIndex.value = 0
    }

    /**
     * Updates sort option and resets car navigation index.
     */
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _currentCarIndex.value = 0
    }

    /**
     * Toggles favourite status for a given car ID.
     */
    fun toggleFavourite(carId: String) {
        val current = _favourites.value.toMutableSet()
        if (current.contains(carId)) {
            current.remove(carId)
        } else {
            current.add(carId)
        }
        _favourites.value = current
    }

    /**
     * Returns true if carId is marked as favourite.
     */
    fun isFavourite(carId: String): Boolean {
        return _favourites.value.contains(carId)
    }

    /**
     * Gets all favourite cars regardless of availability (a rented car may remain favourite).
     */
    fun getFavouriteCars(): List<Car> {
        val favSet = _favourites.value
        return _canonicalCars.filter { it.id in favSet }
    }

    /**
     * Processes a booking for [car] for [rentalDays].
     * Deducts credits and records booking only if validation succeeds.
     */
    fun processBooking(car: Car, rentalDays: Int): ValidationResult {
        val isAlreadyRented = _rentedBookings.value.any { it.car.id == car.id }
        val validation = CarRentalDomain.validateRental(
            car = car,
            rentalDays = rentalDays,
            currentBalance = _creditBalance.value,
            isAlreadyRented = isAlreadyRented
        )

        if (validation is ValidationResult.Success) {
            val totalCost = CarRentalDomain.calculateTotalCost(car.dailyCost, rentalDays)
            _creditBalance.value -= totalCost
            val booking = RentalBooking(car, rentalDays, totalCost)
            _rentedBookings.value = _rentedBookings.value + booking
            _currentCarIndex.value = 0 // Reset index for remaining available cars
        }
        return validation
    }

    /**
     * Returns a rented car back to availability and refunds the rental credits.
     * Fulfills reversal of committed booking requirement.
     */
    fun returnRentedCar(carId: String): Boolean {
        val booking = _rentedBookings.value.find { it.car.id == carId } ?: return false
        _rentedBookings.value = _rentedBookings.value.filterNot { it.car.id == carId }
        _creditBalance.value += booking.totalCost
        return true
    }

    /**
     * Resets repository state to initial values (useful for testing).
     */
    fun resetState() {
        _creditBalance.value = CarRentalDomain.INITIAL_CREDIT_BALANCE
        _favourites.value = emptySet()
        _rentedBookings.value = emptyList()
        _searchQuery.value = ""
        _sortOption.value = SortOption.RATING_DESC
        _currentCarIndex.value = 0
    }
}

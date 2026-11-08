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

    // 5 distinct sample cars with realistic, internally consistent data, detailed specs, and high-res images
    val sampleCars = listOf(
        Car(
            id = "car_001",
            name = "Toyota Camry",
            model = "Camry SE",
            year = 2022,
            rating = 4.8f,
            kmsDriven = 24500,
            dailyCost = 50,
            imageResId = R.drawable.img_toyota_camry,
            engine = "2.5L 4-Cylinder",
            seats = 5,
            color = "Celestial Silver",
            mileage = "16.55 kmpl",
            transmission = "8-speed automatic",
            fuelType = "Gasoline",
            description = "A reliable and fuel-efficient sedan designed for comfortable daily driving. Offers a smooth ride, responsive handling, driver assistance safety features, and a spacious quiet cabin perfect for city and highway trips."
        ),
        Car(
            id = "car_002",
            name = "Tesla Model 3",
            model = "Long Range Dual Motor",
            year = 2023,
            rating = 4.9f,
            kmsDriven = 12000,
            dailyCost = 75,
            imageResId = R.drawable.img_tesla_model3,
            engine = "Dual AC Motors",
            seats = 5,
            color = "Pearl Red Metallic",
            mileage = "576 km range",
            transmission = "Single-speed fixed gear",
            fuelType = "Electric (BEV)",
            description = "High-performance all-electric luxury sedan featuring instant torque, Autopilot highway assist, minimalist glass-roof interior, and ultra-low running costs with zero emissions."
        ),
        Car(
            id = "car_003",
            name = "Hyundai Tucson",
            model = "Tucson SEL",
            year = 2021,
            rating = 4.5f,
            kmsDriven = 38000,
            dailyCost = 45,
            imageResId = R.drawable.img_hyundai_tucson,
            engine = "2.5L Smartstream",
            seats = 5,
            color = "Deep Navy Metallic",
            mileage = "13.80 kmpl",
            transmission = "8-speed automatic",
            fuelType = "Gasoline",
            description = "A versatile compact SUV with bold parametric styling, generous rear cargo volume, high ground clearance, and an intuitive infotainment suite built for family weekend road trips."
        ),
        Car(
            id = "car_004",
            name = "BMW 3 Series",
            model = "330i xDrive",
            year = 2024,
            rating = 4.7f,
            kmsDriven = 8500,
            dailyCost = 90,
            imageResId = R.drawable.img_bmw_3series,
            engine = "2.0L TwinPower Turbo",
            seats = 5,
            color = "Black Sapphire Metallic",
            mileage = "14.20 kmpl",
            transmission = "8-speed Steptronic Sport",
            fuelType = "Premium Gasoline",
            description = "The benchmark sports luxury sedan with intelligent xDrive all-wheel drive, razor-sharp steering dynamics, premium Vernasca leather seats, and cutting-edge curved display."
        ),
        Car(
            id = "car_005",
            name = "Honda Civic",
            model = "Civic EX",
            year = 2020,
            rating = 4.3f,
            kmsDriven = 52000,
            dailyCost = 35,
            imageResId = R.drawable.img_honda_civic,
            engine = "1.5L Turbocharged",
            seats = 5,
            color = "Rallye Blue",
            mileage = "17.80 kmpl",
            transmission = "CVT Automatic",
            fuelType = "Gasoline",
            description = "Sporty, agile, and supremely economical compact hatchback offering exceptional fuel economy, Honda Sensing safety tech, and an ergonomic cockpit tailored for effortless urban navigation."
        )
    )

    private val _creditBalance = MutableStateFlow(CarRentalDomain.INITIAL_CREDIT_BALANCE)
    val creditBalance: StateFlow<Int> = _creditBalance.asStateFlow()

    private val _favourites = MutableStateFlow<Set<String>>(emptySet())
    val favourites: StateFlow<Set<String>> = _favourites.asStateFlow()

    private val _favouriteCars = MutableStateFlow<List<Car>>(emptyList())
    val favouriteCars: StateFlow<List<Car>> = _favouriteCars.asStateFlow()

    private val _rentedBookings = MutableStateFlow<List<RentalBooking>>(emptyList())
    val rentedBookings: StateFlow<List<RentalBooking>> = _rentedBookings.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.RATING_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _currentCarIndex = MutableStateFlow(0)
    val currentCarIndex: StateFlow<Int> = _currentCarIndex.asStateFlow()

    private val _availableCars = MutableStateFlow<List<Car>>(emptyList())
    val availableCars: StateFlow<List<Car>> = _availableCars.asStateFlow()

    init {
        updateComputedCars()
        updateFavouriteCars()
    }

    /**
     * Recomputes the available list and keeps the current index inside its bounds.
     */
    private fun updateComputedCars() {
        val available = getAvailableCars()
        _availableCars.value = available
        _currentCarIndex.value = _currentCarIndex.value.coerceIn(0, (available.size - 1).coerceAtLeast(0))
    }

    private fun updateFavouriteCars() {
        _favouriteCars.value = getFavouriteCars()
    }

    /**
     * Gets the list of available cars (excluding rented cars), filtered by search query and sorted.
     */
    fun getAvailableCars(): List<Car> {
        val rentedIds = _rentedBookings.value.map { it.car.id }.toSet()
        return CarRentalDomain.getAvailableCars(
            allCars = sampleCars,
            rentedCarIds = rentedIds,
            searchQuery = _searchQuery.value,
            sortOption = _sortOption.value
        )
    }

    /**
     * Advances to the next available car in the list (cycling).
     */
    fun nextCar() {
        val size = _availableCars.value.size
        _currentCarIndex.value = if (size == 0) 0 else (_currentCarIndex.value + 1) % size
    }

    /**
     * Sets the active car index (e.g. when swiping or scrolling carousel),
     * ensuring state persists across screen orientation changes.
     */
    fun setCurrentCarIndex(index: Int) {
        val lastIndex = _availableCars.value.lastIndex
        if (lastIndex >= 0) {
            _currentCarIndex.value = index.coerceIn(0, lastIndex)
        }
    }

    /**
     * Updates search query and resets car navigation index.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _currentCarIndex.value = 0
        updateComputedCars()
    }

    /**
     * Updates sort option and resets car navigation index.
     */
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _currentCarIndex.value = 0
        updateComputedCars()
    }

    /**
     * Toggles favourite status for a given car ID.
     */
    fun toggleFavourite(carId: String) {
        val current = _favourites.value
        _favourites.value = if (carId in current) current - carId else current + carId
        updateFavouriteCars()
    }

    /**
     * Gets all favourite cars regardless of availability (a rented car may remain favourite).
     */
    fun getFavouriteCars(): List<Car> {
        val favSet = _favourites.value
        return sampleCars.filter { it.id in favSet }
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
            _rentedBookings.value = _rentedBookings.value + RentalBooking(car, rentalDays, totalCost)
            _currentCarIndex.value = 0 // Reset index for remaining available cars
            updateComputedCars()
        }
        return validation
    }

    /**
     * Returns a rented car back to availability and refunds the rental credits.
     * Fulfills reversal of committed booking requirement.
     *
     * @return the returned booking, or null if the car was not rented.
     */
    fun returnRentedCar(carId: String): RentalBooking? {
        val booking = _rentedBookings.value.find { it.car.id == carId } ?: return null
        _rentedBookings.value = _rentedBookings.value - booking
        _creditBalance.value += booking.totalCost
        updateComputedCars()
        return booking
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
        updateComputedCars()
        updateFavouriteCars()
    }
}

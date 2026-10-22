package com.example.myapplication.domain

import com.example.myapplication.model.Car
import com.example.myapplication.model.SortOption

/**
 * Result of validating a rental request against application business rules.
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

/**
 * Pure domain logic for car rental calculations, validations, filtering, and sorting.
 * Separated from UI layers for complete unit testability.
 */
object CarRentalDomain {

    const val INITIAL_CREDIT_BALANCE = 500
    const val MAX_RENTAL_COST = 400
    const val MIN_RENTAL_DAYS = 1
    const val MAX_RENTAL_DAYS = 7

    /**
     * Calculates the total rental cost dynamically based on daily rate and days.
     */
    fun calculateTotalCost(dailyCost: Int, rentalDays: Int): Int {
        return dailyCost * rentalDays
    }

    /**
     * Validates business rules for booking a vehicle.
     * - Rental duration must be between 1 and 7 days.
     * - Car must still be available (not already rented).
     * - Total rental cost must not exceed 400 credits.
     * - Total rental cost must not exceed the user's current credit balance.
     */
    fun validateRental(
        car: Car,
        rentalDays: Int,
        currentBalance: Int,
        isAlreadyRented: Boolean = false
    ): ValidationResult {
        if (isAlreadyRented) {
            return ValidationResult.Error("This vehicle has already been rented.")
        }
        if (rentalDays < MIN_RENTAL_DAYS || rentalDays > MAX_RENTAL_DAYS) {
            return ValidationResult.Error("Rental duration must be between $MIN_RENTAL_DAYS and $MAX_RENTAL_DAYS days.")
        }
        val totalCost = calculateTotalCost(car.dailyCost, rentalDays)
        if (totalCost > MAX_RENTAL_COST) {
            return ValidationResult.Error("Individual rental cost ($totalCost credits) exceeds the $MAX_RENTAL_COST credit limit.")
        }
        if (totalCost > currentBalance) {
            return ValidationResult.Error("Insufficient credit balance ($currentBalance credits available, $totalCost credits required).")
        }
        return ValidationResult.Success
    }

    /**
     * Searches cars by name and model (case-insensitive).
     */
    fun searchCars(cars: List<Car>, query: String): List<Car> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return cars.toList()
        return cars.filter { car ->
            car.name.contains(trimmed, ignoreCase = true) ||
                    car.model.contains(trimmed, ignoreCase = true)
        }
    }

    /**
     * Sorts cars based on the selected [SortOption] without mutating the canonical dataset.
     */
    fun sortCars(cars: List<Car>, sortOption: SortOption): List<Car> {
        return when (sortOption) {
            SortOption.RATING_DESC -> cars.sortedByDescending { it.rating }
            SortOption.YEAR_DESC -> cars.sortedByDescending { it.year }
            SortOption.COST_ASC -> cars.sortedBy { it.dailyCost }
        }
    }

    /**
     * Filters available cars (excluding rented cars), applies search query, and applies sorting.
     */
    fun getAvailableCars(
        allCars: List<Car>,
        rentedCarIds: Set<String>,
        searchQuery: String,
        sortOption: SortOption
    ): List<Car> {
        val available = allCars.filterNot { it.id in rentedCarIds }
        val searched = searchCars(available, searchQuery)
        return sortCars(searched, sortOption)
    }
}

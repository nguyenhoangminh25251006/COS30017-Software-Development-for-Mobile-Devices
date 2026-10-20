package com.example.myapplication

import com.example.myapplication.domain.CarRentalDomain
import com.example.myapplication.domain.ValidationResult
import com.example.myapplication.model.Car
import com.example.myapplication.model.SortOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for pure business domain logic in [CarRentalDomain].
 */
class CarRentalDomainTest {

    private val testCar = Car(
        id = "test_01",
        name = "Toyota Camry",
        model = "Camry SE",
        year = 2022,
        rating = 4.8f,
        kmsDriven = 20000,
        dailyCost = 50,
        imageResId = 0
    )

    private val premiumCar = Car(
        id = "test_02",
        name = "BMW 3 Series",
        model = "330i xDrive",
        year = 2024,
        rating = 4.7f,
        kmsDriven = 5000,
        dailyCost = 90,
        imageResId = 0
    )

    private val sampleCars = listOf(
        Car("c1", "Toyota Camry", "SE", 2022, 4.8f, 20000, 50, 0),
        Car("c2", "Tesla Model 3", "Long Range", 2023, 4.9f, 10000, 75, 0),
        Car("c3", "Hyundai Tucson", "SEL", 2021, 4.5f, 30000, 45, 0),
        Car("c4", "BMW 3 Series", "330i", 2024, 4.7f, 5000, 90, 0),
        Car("c5", "Honda Civic", "EX", 2020, 4.3f, 50000, 35, 0)
    )

    @Test
    fun testTotalCostCalculation() {
        assertEquals(50, CarRentalDomain.calculateTotalCost(dailyCost = 50, rentalDays = 1))
        assertEquals(350, CarRentalDomain.calculateTotalCost(dailyCost = 50, rentalDays = 7))
        assertEquals(630, CarRentalDomain.calculateTotalCost(dailyCost = 90, rentalDays = 7))
    }

    @Test
    fun testRentalDurationBoundaries_Valid() {
        // Boundary 1 day
        val result1 = CarRentalDomain.validateRental(testCar, rentalDays = 1, currentBalance = 500)
        assertTrue(result1 is ValidationResult.Success)

        // Boundary 7 days (50 * 7 = 350 <= 400, <= 500)
        val result7 = CarRentalDomain.validateRental(testCar, rentalDays = 7, currentBalance = 500)
        assertTrue(result7 is ValidationResult.Success)
    }

    @Test
    fun testRentalDurationBoundaries_Invalid() {
        // Invalid 0 days
        val result0 = CarRentalDomain.validateRental(testCar, rentalDays = 0, currentBalance = 500)
        assertTrue(result0 is ValidationResult.Error)

        // Invalid 8 days
        val result8 = CarRentalDomain.validateRental(testCar, rentalDays = 8, currentBalance = 500)
        assertTrue(result8 is ValidationResult.Error)
    }

    @Test
    fun testRentalCostExact400Credits() {
        val car80 = testCar.copy(dailyCost = 80)
        // 80 * 5 days = 400 credits exactly
        val result = CarRentalDomain.validateRental(car80, rentalDays = 5, currentBalance = 500)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testRentalCostExceeds400Credits() {
        // 90 * 5 days = 450 credits (> 400 credits max limit)
        val result = CarRentalDomain.validateRental(premiumCar, rentalDays = 5, currentBalance = 500)
        assertTrue(result is ValidationResult.Error)
        val error = result as ValidationResult.Error
        assertTrue(error.message.contains("400 credit limit"))
    }

    @Test
    fun testCreditBalanceCheck_Sufficient() {
        // 50 * 4 = 200 credits <= 250 credits balance
        val result = CarRentalDomain.validateRental(testCar, rentalDays = 4, currentBalance = 250)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testCreditBalanceCheck_Insufficient() {
        // 50 * 4 = 200 credits > 150 credits balance
        val result = CarRentalDomain.validateRental(testCar, rentalDays = 4, currentBalance = 150)
        assertTrue(result is ValidationResult.Error)
        val error = result as ValidationResult.Error
        assertTrue(error.message.contains("Insufficient credit balance"))
    }

    @Test
    fun testAlreadyRentedCarValidation() {
        val result = CarRentalDomain.validateRental(
            car = testCar,
            rentalDays = 2,
            currentBalance = 500,
            isAlreadyRented = true
        )
        assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun testSorting_RatingDesc() {
        val sorted = CarRentalDomain.sortCars(sampleCars, SortOption.RATING_DESC)
        assertEquals("Tesla Model 3", sorted[0].name) // 4.9
        assertEquals("Toyota Camry", sorted[1].name)  // 4.8
        assertEquals("BMW 3 Series", sorted[2].name)  // 4.7
        assertEquals("Hyundai Tucson", sorted[3].name)// 4.5
        assertEquals("Honda Civic", sorted[4].name)   // 4.3
    }

    @Test
    fun testSorting_YearDesc() {
        val sorted = CarRentalDomain.sortCars(sampleCars, SortOption.YEAR_DESC)
        assertEquals(2024, sorted[0].year) // BMW
        assertEquals(2023, sorted[1].year) // Tesla
        assertEquals(2022, sorted[2].year) // Toyota
        assertEquals(2021, sorted[3].year) // Hyundai
        assertEquals(2020, sorted[4].year) // Honda
    }

    @Test
    fun testSorting_CostAsc() {
        val sorted = CarRentalDomain.sortCars(sampleCars, SortOption.COST_ASC)
        assertEquals(35, sorted[0].dailyCost) // Honda
        assertEquals(45, sorted[1].dailyCost) // Hyundai
        assertEquals(50, sorted[2].dailyCost) // Toyota
        assertEquals(75, sorted[3].dailyCost) // Tesla
        assertEquals(90, sorted[4].dailyCost) // BMW
    }

    @Test
    fun testSortingPreservesCanonicalDataset() {
        val originalList = sampleCars.toList()
        CarRentalDomain.sortCars(sampleCars, SortOption.COST_ASC)
        // Verify input list was not mutated
        assertEquals(originalList, sampleCars)
    }

    @Test
    fun testSearchByNameAndModel() {
        val searchToyota = CarRentalDomain.searchCars(sampleCars, "Toyota")
        assertEquals(1, searchToyota.size)
        assertEquals("Toyota Camry", searchToyota[0].name)

        val searchModel = CarRentalDomain.searchCars(sampleCars, "Long Range")
        assertEquals(1, searchModel.size)
        assertEquals("Tesla Model 3", searchModel[0].name)

        val searchCaseInsensitive = CarRentalDomain.searchCars(sampleCars, "bmw")
        assertEquals(1, searchCaseInsensitive.size)
        assertEquals("BMW 3 Series", searchCaseInsensitive[0].name)

        val searchNoMatch = CarRentalDomain.searchCars(sampleCars, "Ferrari")
        assertTrue(searchNoMatch.isEmpty())
    }
}

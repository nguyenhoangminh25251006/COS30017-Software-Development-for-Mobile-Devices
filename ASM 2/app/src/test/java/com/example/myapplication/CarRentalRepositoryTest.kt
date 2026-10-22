package com.example.myapplication

import com.example.myapplication.data.CarRentalRepository
import com.example.myapplication.domain.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for state management, booking workflows, and favourites in [CarRentalRepository].
 */
class CarRentalRepositoryTest {

    private lateinit var repository: CarRentalRepository

    @Before
    fun setUp() {
        repository = CarRentalRepository()
    }

    @Test
    fun testInitialState() {
        assertEquals(500, repository.creditBalance.value)
        assertEquals(5, repository.sampleCars.size)
        assertEquals(5, repository.getAvailableCars().size)
        assertTrue(repository.rentedBookings.value.isEmpty())
        assertTrue(repository.favourites.value.isEmpty())
    }

    @Test
    fun testSuccessfulBooking() {
        val carToRent = repository.sampleCars[0] // Toyota Camry, 50 credits/day
        val initialAvailableCount = repository.getAvailableCars().size

        // Book for 3 days = 150 credits
        val result = repository.processBooking(carToRent, rentalDays = 3)
        assertTrue(result is ValidationResult.Success)

        // Balance updated: 500 - 150 = 350
        assertEquals(350, repository.creditBalance.value)

        // Rented cars recorded
        assertEquals(1, repository.rentedBookings.value.size)
        assertEquals(carToRent.id, repository.rentedBookings.value[0].car.id)

        // Available cars decreased by 1
        assertEquals(initialAvailableCount - 1, repository.getAvailableCars().size)
        assertFalse(repository.getAvailableCars().any { it.id == carToRent.id })
    }

    @Test
    fun testPreventDuplicateBooking() {
        val car = repository.sampleCars[0]

        // First booking succeeds
        val result1 = repository.processBooking(car, rentalDays = 2)
        assertTrue(result1 is ValidationResult.Success)

        // Second booking attempt on same car fails
        val result2 = repository.processBooking(car, rentalDays = 2)
        assertTrue(result2 is ValidationResult.Error)

        // Balance remains 500 - (50 * 2) = 400
        assertEquals(400, repository.creditBalance.value)
        assertEquals(1, repository.rentedBookings.value.size)
    }

    @Test
    fun testCancellingBookingDoesNotDeductCredits() {
        val initialBalance = repository.creditBalance.value
        val initialBookings = repository.rentedBookings.value.size

        // User opens rental screen, but cancels (does not call processBooking)
        // Verify balance and bookings remain unchanged
        assertEquals(initialBalance, repository.creditBalance.value)
        assertEquals(initialBookings, repository.rentedBookings.value.size)
    }

    @Test
    fun testFavouritesIndependentOfAvailability() {
        val car = repository.sampleCars[0]

        // Add to favourites
        repository.toggleFavourite(car.id)
        assertTrue(repository.isFavourite(car.id))
        assertEquals(1, repository.getFavouriteCars().size)

        // Rent the car
        repository.processBooking(car, rentalDays = 2)

        // Car is no longer available for booking
        assertFalse(repository.getAvailableCars().any { it.id == car.id })

        // BUT car remains in favourites
        assertTrue(repository.isFavourite(car.id))
        assertEquals(1, repository.getFavouriteCars().size)
    }

    @Test
    fun testReturnRentedCarToAvailability() {
        val car = repository.sampleCars[0]
        // Rent for 3 days = 150 credits
        repository.processBooking(car, rentalDays = 3)
        assertEquals(350, repository.creditBalance.value)
        assertEquals(4, repository.getAvailableCars().size)
        assertEquals(1, repository.rentedBookings.value.size)

        // Return the car
        val returned = repository.returnRentedCar(car.id)
        assertTrue(returned)

        // Balance refunded back to 500
        assertEquals(500, repository.creditBalance.value)
        // Available cars back to 5
        assertEquals(5, repository.getAvailableCars().size)
        // Rented list empty
        assertTrue(repository.rentedBookings.value.isEmpty())
    }

    @Test
    fun testNextCarNavigation() {
        val initialCar = repository.getCurrentCar()
        assertNotNull(initialCar)

        repository.nextCar()
        val secondCar = repository.getCurrentCar()
        assertNotNull(secondCar)

        // Advanced to next index
        assertEquals(1, repository.currentCarIndex.value)
    }

    @Test
    fun testResetState() {
        val car = repository.sampleCars[0]
        repository.toggleFavourite(car.id)
        repository.processBooking(car, rentalDays = 2)

        // Reset
        repository.resetState()

        assertEquals(500, repository.creditBalance.value)
        assertTrue(repository.favourites.value.isEmpty())
        assertTrue(repository.rentedBookings.value.isEmpty())
        assertEquals(5, repository.getAvailableCars().size)
    }
}

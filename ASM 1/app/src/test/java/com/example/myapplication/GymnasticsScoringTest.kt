package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests verifying scoring logic and rules for Gymnastics App assignment requirements.
 */
class GymnasticsScoringTest {

    private var score: Int = 0
    private var currentElement: Int = 0
    private var isDeductionTaken: Boolean = false

    @Before
    fun setUp() {
        score = 0
        currentElement = 0
        isDeductionTaken = false
    }

    private fun getPointsForElement(element: Int): Int {
        return when (element) {
            in 1..3 -> 1   // Basic
            in 4..7 -> 2   // Intermediate
            in 8..10 -> 3  // Advanced
            else -> 0
        }
    }

    private fun performNextElement() {
        if (currentElement >= 10 || isDeductionTaken) return
        currentElement++
        val points = getPointsForElement(currentElement)
        score = (score + points).coerceAtMost(20)
    }

    private fun takeDeduction() {
        if (currentElement < 1 || currentElement >= 10 || isDeductionTaken) return
        score = (score - 2).coerceAtLeast(0)
        isDeductionTaken = true
    }

    private fun resetRoutine() {
        score = 0
        currentElement = 0
        isDeductionTaken = false
    }

    @Test
    fun testInitialState() {
        assertEquals(0, score)
        assertEquals(0, currentElement)
        assertFalse(isDeductionTaken)
    }

    @Test
    fun testPointsPerZoneAndMaxScore() {
        // Elements 1..3: Basic (+1 pt each) -> score = 3
        repeat(3) { performNextElement() }
        assertEquals(3, currentElement)
        assertEquals(3, score)

        // Elements 4..7: Intermediate (+2 pts each) -> score = 3 + 4*2 = 11
        repeat(4) { performNextElement() }
        assertEquals(7, currentElement)
        assertEquals(11, score)

        // Elements 8..10: Advanced (+3 pts each) -> score = 11 + 3*3 = 20
        repeat(3) { performNextElement() }
        assertEquals(10, currentElement)
        assertEquals(20, score)
    }

    @Test
    fun testDeductionNotAllowedBeforeFirstElement() {
        // At element 0, deduction should be ignored
        takeDeduction()
        assertEquals(0, score)
        assertFalse(isDeductionTaken)
    }

    @Test
    fun testDeductionStopsFurtherPerformance() {
        // Perform 2 elements (Basic: 2 pts)
        performNextElement()
        performNextElement()
        assertEquals(2, score)

        // Take deduction (-2 pts) -> score = 0, isDeductionTaken = true
        takeDeduction()
        assertEquals(0, score)
        assertTrue(isDeductionTaken)

        // Trying to perform next element should have no effect
        performNextElement()
        assertEquals(2, currentElement) // element stays 2
        assertEquals(0, score)          // score stays 0
    }

    @Test
    fun testDeductionNotAllowedAfterTenthElement() {
        // Complete all 10 elements
        repeat(10) { performNextElement() }
        assertEquals(10, currentElement)
        assertEquals(20, score)

        // Try to take deduction -> ignored
        takeDeduction()
        assertEquals(20, score)
        assertFalse(isDeductionTaken)
    }

    @Test
    fun testResetRoutine() {
        repeat(5) { performNextElement() }
        takeDeduction()

        resetRoutine()
        assertEquals(0, score)
        assertEquals(0, currentElement)
        assertFalse(isDeductionTaken)
    }
}
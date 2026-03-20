package com.sjn.gym.feature.workout.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlateCalculatorTest {
    private val calculator = PlateCalculator()

    @Test
    fun `parse single weight`() {
        val result = calculator.parse("20")
        assertEquals(1, result.size)
        assertEquals(20.0, result[0].weight, 0.0)
        assertEquals(1, result[0].count)
    }

    @Test
    fun `parse multiple weights`() {
        val result = calculator.parse("20 10")
        // sorted by weight: 10, 20
        assertEquals(2, result.size)
        assertEquals(10.0, result[0].weight, 0.0)
        assertEquals(20.0, result[1].weight, 0.0)
    }

    @Test
    fun `parse quantity x weight`() {
        val result = calculator.parse("3x20")
        assertEquals(1, result.size)
        assertEquals(20.0, result[0].weight, 0.0)
        assertEquals(3, result[0].count)
    }

    @Test
    fun `parse mixed input`() {
        // "7.5 3x12.5 5x15"
        val result = calculator.parse("7.5 3x12.5 5x15")
        // 1x7.5, 3x12.5, 5x15
        assertEquals(3, result.size)

        val p1 = result.first { it.weight == 7.5 }
        assertEquals(1, p1.count)

        val p2 = result.first { it.weight == 12.5 }
        assertEquals(3, p2.count)

        val p3 = result.first { it.weight == 15.0 }
        assertEquals(5, p3.count)
    }

    @Test
    fun `calculate total weight`() {
        val total = calculator.calculateTotalWeight("10 2x20", barWeight = 20.0)
        // 20 (bar) + 10 + 2*20 = 70
        assertEquals(70.0, total, 0.0)
    }
}

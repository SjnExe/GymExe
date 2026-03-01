package com.sjn.gym.feature.workout.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WeightInputParserTest {
    private val parser = WeightInputParser()

    @Test
    fun `parse empty returns empty list`() {
        val result = parser.parse("", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(emptyList<Double>(), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Barbell single returns correct weight`() {
        val result = parser.parse("20", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(listOf(20.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Barbell multiple spaces returns merged weights`() {
        val result = parser.parse("20 10 5", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(listOf(20.0, 10.0, 5.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Barbell multiplier format returns expanded weights`() {
        val result = parser.parse("2x20", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(listOf(20.0, 20.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Barbell mixed format returns correct weights`() {
        val result = parser.parse("2x20 10", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        // 20, 20, 10
        assertEquals(listOf(20.0, 20.0, 10.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Barbell complex merge request`() {
        // "5 5 5 5" -> Should be treated as 4 5s.
        val result = parser.parse("5 5 5 5", "Barbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(listOf(5.0, 5.0, 5.0, 5.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Dumbbell single returns correct weight`() {
        val result = parser.parse("20", "Dumbbell")
        assertTrue(result is WeightParsingResult.Success)
        assertEquals(listOf(20.0), (result as WeightParsingResult.Success).weights)
    }

    @Test
    fun `parse Dumbbell multiple returns failure`() {
        val result = parser.parse("20 20", "Dumbbell")
        assertTrue(result is WeightParsingResult.Failure)
        assertTrue((result as WeightParsingResult.Failure).message.contains("Stacking weights is not allowed"))
    }

    @Test
    fun `parse Dumbbell multiplier returns failure`() {
        val result = parser.parse("2x10", "Dumbbell")
        assertTrue(result is WeightParsingResult.Failure)
    }

    @Test
    fun `parse Dumbbell plus returns failure`() {
        val result = parser.parse("10+10", "Dumbbell")
        assertTrue(result is WeightParsingResult.Failure)
    }

    @Test
    fun `parse Machine returns failure for stack`() {
        val result = parser.parse("10 10", "Machine")
        assertTrue(result is WeightParsingResult.Failure)
    }
}

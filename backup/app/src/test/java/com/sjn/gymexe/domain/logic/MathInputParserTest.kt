package com.sjn.gymexe.domain.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MathInputParserTest {

    private val parser = MathInputParser()

    @Test
    fun `parse handles simple addition with spaces`() {
        assertEquals(15.0, parser.parse("10 5"), 0.01)
        assertEquals(30.0, parser.parse("10 10 10"), 0.01)
    }

    @Test
    fun `parse handles multiplication`() {
        assertEquals(20.0, parser.parse("2x10"), 0.01)
        assertEquals(20.0, parser.parse("2*10"), 0.01)
        assertEquals(20.0, parser.parse("2X10"), 0.01)
    }

    @Test
    fun `parse handles mixed expressions`() {
        // 20 + 2*10 + 5 = 20 + 20 + 5 = 45
        assertEquals(45.0, parser.parse("20 2x10 5"), 0.01)
    }

    @Test
    fun `parse handles decimals`() {
        assertEquals(12.5, parser.parse("10 2.5"), 0.01)
        assertEquals(7.5, parser.parse("3x2.5"), 0.01)
    }

    @Test
    fun `smartAppend simple append`() {
        assertEquals("20 10", parser.smartAppend("20", 10f))
    }

    @Test
    fun `smartAppend converts to multiplication`() {
        assertEquals("20 2x10", parser.smartAppend("20 10", 10f))
    }

    @Test
    fun `smartAppend increments multiplication`() {
        assertEquals("20 3x10", parser.smartAppend("20 2x10", 10f))
    }

    @Test
    fun `smartAppend handles decimals`() {
        assertEquals("20 2x2.5", parser.smartAppend("20 2.5", 2.5f))
        assertEquals("20 3x2.5", parser.smartAppend("20 2x2.5", 2.5f))
    }
}

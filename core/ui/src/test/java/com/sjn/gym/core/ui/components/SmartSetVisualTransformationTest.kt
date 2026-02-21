package com.sjn.gym.core.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SmartSetVisualTransformationTest {

    private val setsColor = Color(0xFF4CAF50)
    private val separatorColor = Color(0xFF9E9E9E)
    private val weightColor = Color(0xFF2196F3)

    private val transformation = SmartSetVisualTransformation(
        setsColor = setsColor,
        separatorColor = separatorColor,
        weightColor = weightColor
    )

    @Test
    fun filter_correctlyFormatsStandardInput_3x10() {
        val input = AnnotatedString("3x10")
        val result = transformation.filter(input)
        val annotatedString = result.text

        // Verify content
        assertEquals("3x10", annotatedString.text)

        // Verify styles
        // Expected: "3" (green), "x" (grey), "10" (blue)
        val spanStyles = annotatedString.spanStyles

        val greenSpans = spanStyles.filter { it.item.color == setsColor }
        val greySpans = spanStyles.filter { it.item.color == separatorColor }
        val blueSpans = spanStyles.filter { it.item.color == weightColor }

        assertEquals(1, greenSpans.size)
        assertEquals(0, greenSpans[0].start)
        assertEquals(1, greenSpans[0].end) // "3"

        assertEquals(1, greySpans.size)
        assertEquals(1, greySpans[0].start)
        assertEquals(2, greySpans[0].end) // "x"

        assertEquals(1, blueSpans.size)
        assertEquals(2, blueSpans[0].start)
        assertEquals(4, blueSpans[0].end) // "10"
    }
}

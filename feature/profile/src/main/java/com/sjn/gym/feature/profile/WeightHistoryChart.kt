package com.sjn.gym.feature.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * A simple line chart to visualize weight history. Currently uses placeholder data. Later, actual
 * weight data points (timestamp -> value) can be mapped.
 */
@Composable
fun WeightHistoryChart(
    modifier: Modifier = Modifier,
    dataPoints: List<Float> = listOf(70f, 72f, 71.5f, 73f, 72.8f, 74f, 75f), // Placeholder data
    lineColor: Color = MaterialTheme.colorScheme.primary,
) {
    if (dataPoints.isEmpty()) {
        return
    }

    val maxDataValue = dataPoints.maxOrNull() ?: 1f
    val minDataValue = dataPoints.minOrNull() ?: 0f

    // Add some padding to the Y axis min/max so lines don't hug the very edge perfectly
    val yMin = max(0f, minDataValue - (maxDataValue - minDataValue) * 0.2f)
    val yMax = maxDataValue + (maxDataValue - minDataValue) * 0.2f

    Canvas(
        modifier =
            modifier.fillMaxWidth().height(150.dp).padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        val width = size.width
        val height = size.height

        val stepX = if (dataPoints.size > 1) width / (dataPoints.size - 1) else width
        val rangeY = if (yMax > yMin) yMax - yMin else 1f

        val path = Path()

        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - ((value - yMin) / rangeY * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // Draw a small circle at each data point
            drawCircle(color = lineColor, radius = 4.dp.toPx(), center = Offset(x, y))
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

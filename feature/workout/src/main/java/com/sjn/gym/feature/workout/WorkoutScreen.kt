package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sjn.gym.feature.workout.logic.PlateCount

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel = hiltViewModel()) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(
            text = "Log Set",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Colors
        val quantityColor = MaterialTheme.colorScheme.primary
        val separatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        val weightColor = MaterialTheme.colorScheme.tertiary
        val defaultColor = MaterialTheme.colorScheme.onSurface

        // Annotated String for Syntax Highlighting
        val annotatedString =
            buildAnnotatedString {
                val text = viewModel.input
                // Regex-based parser for highlighting:
                // 1. Quantity: Digits immediately followed by 'x' or '*'
                // 2. Separator: 'x' or '*' or '+'
                // 3. Weight: Digits immediately following 'x' or '*' OR digits not followed by 'x' or '*'

                // Helper to determine the type of a token or character range would be complex with simple iteration.
                // We will use Regex to find matches and color them.
                // However, we must ensure we append the entire string in order.

                // Strategy: Iterate through the string, checking current position against known patterns?
                // Or simpler: Build the string, and keep track of "current style".

                // Let's identify the ranges first.
                // Pattern for "Quantity x Weight": (\d+)\s*([xX*])\s*(\d+(\.\d+)?)
                // Pattern for "Weight": (\d+(\.\d+)?)(?![xX*]) -- this lookahead is tricky if not careful.

                // Let's perform a simple tokenization loop.
                var i = 0
                while (i < text.length) {
                    val char = text[i]

                    if (char.isDigit() || char == '.') {
                        // Scan the full number
                        val start = i
                        while (i < text.length && (text[i].isDigit() || text[i] == '.')) {
                            i++
                        }
                        val numberStr = text.substring(start, i)

                        // Look ahead for separator
                        var isQuantity = false
                        var lookAheadIndex = i
                        // Skip whitespace
                        while (lookAheadIndex < text.length && text[lookAheadIndex].isWhitespace()) {
                            lookAheadIndex++
                        }
                        if (lookAheadIndex < text.length) {
                            val nextChar = text[lookAheadIndex]
                            if (nextChar == 'x' || nextChar == 'X' || nextChar == '*') {
                                isQuantity = true
                            }
                        }

                        withStyle(
                            style =
                                SpanStyle(
                                    color = if (isQuantity) quantityColor else weightColor,
                                    fontWeight = FontWeight.Bold,
                                ),
                        ) {
                            append(numberStr)
                        }
                    } else if (char == 'x' || char == 'X' || char == '*' || char == '+') {
                        withStyle(style = SpanStyle(color = separatorColor, fontWeight = FontWeight.Bold)) {
                            append(char)
                        }
                        i++
                    } else {
                        withStyle(style = SpanStyle(color = defaultColor)) {
                            append(char)
                        }
                        i++
                    }
                }
            }

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentAlignment = androidx.compose.ui.Alignment.CenterStart,
            ) {
                if (viewModel.input.isEmpty()) {
                    Text(
                        text = "Enter Plates (e.g. 2x20)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                BasicTextField(
                    value = viewModel.input,
                    onValueChange = { viewModel.onInputChange(it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = {
                        TransformedText(annotatedString, OffsetMapping.Identity)
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Total Weight: ${viewModel.plateResult.sumOf { it.weight * it.count }}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            viewModel.plateResult.forEach { plate ->
                PlateChip(plate)
            }
        }
    }
}

@Composable
fun PlateChip(plate: PlateCount) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(
                text = "${plate.count}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = " x ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
            )
            Text(
                text = "${plate.weight}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

package com.sjn.gym.feature.workout

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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

        // Annotated String for Syntax Highlighting
        val annotatedString =
            buildAnnotatedString {
                val text = viewModel.input
                // Simple parser for highlighting:
                // Digits -> Primary Color
                // 'x' or '*' -> Secondary/Tertiary Color
                // Others -> OnSurface

                var currentIndex = 0
                while (currentIndex < text.length) {
                    val char = text[currentIndex]
                    when {
                        char.isDigit() || char == '.' -> {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                                append(char)
                            }
                        }
                        char.lowercaseChar() == 'x' || char == '*' -> {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)) {
                                append(char)
                            }
                        }
                        else -> {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                append(char)
                            }
                        }
                    }
                    currentIndex++
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
                    }
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

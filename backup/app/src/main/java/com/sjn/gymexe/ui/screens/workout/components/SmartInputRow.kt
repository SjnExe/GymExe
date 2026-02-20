package com.sjn.gymexe.ui.screens.workout.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sjn.gymexe.domain.logic.MathInputParser
import com.sjn.gymexe.domain.model.ExerciseStats

@Composable
fun SmartInputRow(
    weightInput: String,
    onWeightInputChange: (String) -> Unit,
    repsInput: String,
    onRepsInputChange: (String) -> Unit,
    onLogSet: () -> Unit,
    parser: MathInputParser,
    availablePlates: List<Float>,
    availableDumbbells: List<Float>,
    equipmentCategory: String, // "BARBELL", "DUMBBELL", "MACHINE", "CABLE", "BODYWEIGHT"
    machineIncrement: Float?,
    machineMax: Float?,
    exerciseStats: ExerciseStats?,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    // Live calculation of the total weight
    val calculatedWeight = remember(weightInput) {
        parser.parse(weightInput)
    }

    // Determine which weights to show in the accessory bar
    val accessoryWeights = remember(equipmentCategory, availablePlates, availableDumbbells, machineIncrement, machineMax) {
        when (equipmentCategory) {
            "BARBELL" -> availablePlates
            "DUMBBELL" -> availableDumbbells
            "MACHINE", "CABLE" -> {
                val step = machineIncrement ?: 5f
                val max = machineMax ?: 100f
                generateSequence(step) { it + step }.takeWhile { it <= max }.toList()
            }
            "BODYWEIGHT" -> listOf(0f, 2.5f, 5f, 10f, 15f, 20f)
            else -> emptyList()
        }
    }

    Column(modifier = modifier) {
        // Row 1: Weight Increments (Plates/Dumbbells)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            accessoryWeights.forEach { weight ->
                SuggestionChip(
                    onClick = {
                        val newValue = parser.smartAppend(weightInput, weight)
                        onWeightInputChange(newValue)
                    },
                    label = {
                        Text(
                            text = if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString()
                        )
                    }
                )
            }
        }

        // Row 2: History Suggestions (Last Used, PR)
        if (exerciseStats != null && (exerciseStats.lastUsedWeight != null || exerciseStats.personalBest != null)) {
             Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Last Used Chip
                exerciseStats.lastUsedWeight?.let { weight ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            // Replace current input with Last Used? Or Append?
                            // Usually "Last Used" implies "I want to do this weight again".
                            // So let's replace.
                            onWeightInputChange(if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString())
                        },
                        label = { Text("Last: ${if (weight % 1.0 == 0.0) weight.toInt() else weight}kg") }
                    )
                }

                // PR Chip
                exerciseStats.personalBest?.let { weight ->
                    FilterChip(
                        selected = false,
                        onClick = {
                             onWeightInputChange(if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString())
                        },
                        label = { Text("PR: ${if (weight % 1.0 == 0.0) weight.toInt() else weight}kg") }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            // Weight Input
            OutlinedTextField(
                value = weightInput,
                onValueChange = onWeightInputChange,
                label = { Text("Weight") },
                supportingText = {
                    if (weightInput.isNotEmpty()) {
                        Text("= ${if (calculatedWeight % 1.0 == 0.0) calculatedWeight.toInt() else calculatedWeight} kg")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )

            // Reps Input
            OutlinedTextField(
                value = repsInput,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        onRepsInputChange(newValue)
                    }
                },
                label = { Text("Reps") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLogSet()
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.weight(0.5f),
            )
        }
    }
}

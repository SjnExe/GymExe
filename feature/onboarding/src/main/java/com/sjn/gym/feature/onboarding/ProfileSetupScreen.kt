package com.sjn.gym.feature.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sjn.gym.core.ui.components.SegmentedButton

@Composable
fun ProfileSetupScreen(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    var gender by remember { mutableStateOf<String?>(null) }
    var weightValue by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("KG") }
    var heightValue by remember { mutableStateOf("") }
    var heightUnit by remember { mutableStateOf("CM") }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
        // Increased padding
        horizontalAlignment = Alignment.Start, // Align content to start usually looks better for forms
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Tell us about yourself",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        // Gender Selection
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GenderCard(
                gender = "Male",
                icon = Icons.Filled.Man,
                isSelected = gender == "MALE",
                onClick = { gender = "MALE" },
                modifier = Modifier.weight(1f),
            )
            GenderCard(
                gender = "Female",
                icon = Icons.Filled.Woman,
                isSelected = gender == "FEMALE",
                onClick = { gender = "FEMALE" },
                modifier = Modifier.weight(1f),
            )
        }

        // Weight Input
        Text(
            text = "Weight",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = weightValue,
                onValueChange = { newValue ->
                    if (newValue.all { char -> char.isDigit() || char == '.' } &&
                        newValue.count { it == '.' } <= 1
                    ) {
                        val decimalIndex = newValue.indexOf('.')
                        if (decimalIndex == -1 || newValue.length - decimalIndex <= 3) {
                            weightValue = newValue
                        }
                    }
                },
                label = { Text("Weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
            // SegmentedButton might need explicit size or container
            Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.Center) {
                SegmentedButton(
                    options = listOf("KG", "LBS"),
                    selectedOption = weightUnit,
                    onOptionSelected = { weightUnit = it },
                )
            }
        }

        // Height Input
        Text(
            text = "Height",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = heightValue,
                onValueChange = { newValue ->
                    if (heightUnit == "CM") {
                        if (newValue.all { char -> char.isDigit() || char == '.' } &&
                            newValue.count { it == '.' } <= 1
                        ) {
                            val decimalIndex = newValue.indexOf('.')
                            if (decimalIndex == -1 || newValue.length - decimalIndex <= 2) {
                                heightValue = newValue
                            }
                        }
                    } else {
                        // For Feet, allow digits, ', ", and .
                        if (newValue.all { char -> char.isDigit() || char == '.' || char == '\'' || char == '"' }) {
                            heightValue = newValue
                        }
                    }
                },
                label = { Text(if (heightUnit == "CM") "Height (cm)" else "Height (ft'in\")") },
                // Use Text keyboard to allow special chars for Feet/Inches if needed
                keyboardOptions = KeyboardOptions(keyboardType = if (heightUnit == "FT") KeyboardType.Text else KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
            Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.Center) {
                SegmentedButton(
                    options = listOf("CM", "FT"),
                    selectedOption = heightUnit,
                    onOptionSelected = {
                        // Reset value when switching units to prevent invalid states
                        heightUnit = it
                        heightValue = ""
                    },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Parse Height if FT
                var finalHeight: Double? = null
                if (heightUnit == "CM") {
                    finalHeight = heightValue.toDoubleOrNull()
                } else if (heightUnit == "FT") {
                    // Try parsing 5'11" format
                    try {
                        val feetIndex = heightValue.indexOf('\'')
                        val inchIndex = heightValue.indexOf('"')

                        if (feetIndex != -1) {
                            val feet = heightValue.substring(0, feetIndex).trim().toDouble()
                            val inches =
                                if (inchIndex != -1 && inchIndex > feetIndex) {
                                    heightValue.substring(feetIndex + 1, inchIndex).trim().toDouble()
                                } else if (feetIndex < heightValue.length - 1) {
                                    heightValue.substring(feetIndex + 1).trim().toDoubleOrNull() ?: 0.0
                                } else {
                                    0.0
                                }
                            // Convert to decimal feet: 5'6" -> 5.5
                            finalHeight = feet + (inches / 12.0)
                        } else {
                            // Try parsing as simple double if no ' symbol
                            finalHeight = heightValue.toDoubleOrNull()
                        }
                    } catch (e: Exception) {
                        // ignore parsing error, button validation should handle
                    }
                }

                viewModel.saveProfileData(
                    gender = gender,
                    weight = weightValue.toDoubleOrNull(),
                    weightUnit = weightUnit,
                    height = finalHeight,
                    heightUnit = heightUnit,
                )
                onNext()
            },
            enabled =
                gender != null &&
                    weightValue.isNotEmpty() &&
                    weightValue.toDoubleOrNull()?.let { it > 0 } == true &&
                    heightValue.isNotEmpty() &&
                    (
                        (heightUnit == "CM" && heightValue.toDoubleOrNull()?.let { it > 0 } == true) ||
                            (heightUnit == "FT" && heightValue.any { it.isDigit() })
                    ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Taller button
        ) {
            Text("Next", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun GenderCard(
    gender: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = gender,
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gender,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

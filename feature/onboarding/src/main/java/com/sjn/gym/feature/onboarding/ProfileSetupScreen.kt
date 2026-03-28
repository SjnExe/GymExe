package com.sjn.gym.feature.onboarding

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
import kotlinx.collections.immutable.persistentListOf

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
        modifier = modifier.fillMaxSize().padding(24.dp),
        // Increased padding
        horizontalAlignment =
            Alignment.Start, // Align content to start usually looks better for forms
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = weightValue,
                onValueChange = { newValue ->
                    if (
                        newValue.all { char -> char.isDigit() || char == '.' } &&
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
                    options = persistentListOf("KG", "LBS"),
                    selectedOption = weightUnit,
                    onOptionSelected = { weightUnit = it },
                )
            }
        }

        var heightFeet by remember { mutableStateOf("") }
        var heightInches by remember { mutableStateOf("") }

        // Height Input
        Text(
            text = "Height",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (heightUnit == "CM") {
                OutlinedTextField(
                    value = heightValue,
                    onValueChange = { newValue ->
                        if (
                            newValue.all { char -> char.isDigit() || char == '.' } &&
                                newValue.count { it == '.' } <= 1
                        ) {
                            val decimalIndex = newValue.indexOf('.')
                            if (decimalIndex == -1 || newValue.length - decimalIndex <= 2) {
                                heightValue = newValue
                            }
                        }
                    },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            } else {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = heightFeet,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                heightFeet = newValue
                            }
                        },
                        label = { Text("Ft") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = heightInches,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                val intVal = newValue.toIntOrNull()
                                if (intVal == null || intVal < 12) {
                                    heightInches = newValue
                                }
                            }
                        },
                        label = { Text("In") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
            }

            Box(
                modifier = Modifier.height(56.dp).padding(top = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                SegmentedButton(
                    options = persistentListOf("CM", "FT"),
                    selectedOption = heightUnit,
                    onOptionSelected = {
                        // Reset value when switching units to prevent invalid states
                        heightUnit = it
                        heightValue = ""
                        heightFeet = ""
                        heightInches = ""
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
                    val feet = heightFeet.toDoubleOrNull() ?: 0.0
                    val inches = heightInches.toDoubleOrNull() ?: 0.0
                    // Convert to decimal feet: 5'6" -> 5.5
                    finalHeight = feet + (inches / 12.0)
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
                    ((heightUnit == "CM" &&
                        heightValue.isNotEmpty() &&
                        heightValue.toDoubleOrNull()?.let { it > 0 } == true) ||
                        (heightUnit == "FT" && heightFeet.isNotEmpty())),
            modifier = Modifier.fillMaxWidth().height(56.dp), // Taller button
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
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                contentColor =
                    if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(imageVector = icon, contentDescription = gender, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = gender, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

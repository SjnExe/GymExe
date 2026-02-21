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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sjn.gym.core.ui.components.SegmentedButton

@Composable
fun ProfileSetupScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var gender by remember { mutableStateOf<String?>(null) }
    var weightValue by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf("KG") }
    var heightValue by remember { mutableStateOf("") }
    var heightUnit by remember { mutableStateOf("CM") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Increased padding
        horizontalAlignment = Alignment.Start, // Align content to start usually looks better for forms
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Tell us about yourself",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Gender Selection
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenderCard(
                gender = "Male",
                isSelected = gender == "Male",
                onClick = { gender = "Male" },
                modifier = Modifier.weight(1f)
            )
            GenderCard(
                gender = "Female",
                isSelected = gender == "Female",
                onClick = { gender = "Female" },
                modifier = Modifier.weight(1f)
            )
        }

        // Weight Input
        Text(
            text = "Weight",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = weightValue,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) weightValue = it },
                label = { Text("Weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            // SegmentedButton might need explicit size or container
            Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.Center) {
                 SegmentedButton(
                    options = listOf("KG", "LBS"),
                    selectedOption = weightUnit,
                    onOptionSelected = { weightUnit = it }
                )
            }
        }

        // Height Input
        Text(
            text = "Height",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = heightValue,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) heightValue = it },
                label = { Text("Height") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.Center) {
                SegmentedButton(
                    options = listOf("CM", "FT"),
                    selectedOption = heightUnit,
                    onOptionSelected = { heightUnit = it }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.saveProfileData(
                    gender = gender,
                    weight = weightValue.toDoubleOrNull(),
                    weightUnit = weightUnit,
                    height = heightValue.toDoubleOrNull(),
                    heightUnit = heightUnit
                )
                onNext()
            },
            enabled = gender != null && weightValue.isNotEmpty() && heightValue.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Taller button
        ) {
            Text("Next", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun GenderCard(
    gender: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gender,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Tell us about yourself",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Gender Selection
        Text("Gender", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GenderCard("Male", gender == "Male") { gender = "Male" }
            GenderCard("Female", gender == "Female") { gender = "Female" }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weight Input
        Text("Weight", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = weightValue,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) weightValue = it },
                label = { Text("Weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            SegmentedButton(
                options = listOf("KG", "LBS"),
                selectedOption = weightUnit,
                onOptionSelected = { weightUnit = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Height Input
        Text("Height", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = heightValue,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) heightValue = it },
                label = { Text("Height") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            SegmentedButton(
                options = listOf("CM", "FT"),
                selectedOption = heightUnit,
                onOptionSelected = { heightUnit = it }
            )
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun GenderCard(
    gender: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gender,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

package com.sjn.gym.feature.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun EquipmentSelectionScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val equipmentList =
        listOf(
            "Dumbbells",
            "Barbell",
            "Kettlebell",
            "Pull-up Bar",
            "Cable Machine",
            "Leg Press",
            "Smith Machine",
            "Squat Rack",
            "Bench",
            "Medicine Ball",
            "Resistance Bands",
        )

    var selectedEquipment by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
    ) {
        Text(
            text = "Equipment",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = "Select what you have access to.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = { selectedEquipment = emptySet() },
                modifier = Modifier.weight(1f),
            ) {
                Text("None / Bodyweight")
            }
            OutlinedButton(
                onClick = { selectedEquipment = equipmentList.toSet() },
                modifier = Modifier.weight(1f),
            ) {
                Text("Full Gym Access")
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(equipmentList) { equipment ->
                EquipmentItem(
                    name = equipment,
                    isSelected = selectedEquipment.contains(equipment),
                    onToggle = {
                        selectedEquipment =
                            if (selectedEquipment.contains(equipment)) {
                                selectedEquipment - equipment
                            } else {
                                selectedEquipment + equipment
                            }
                    },
                )
            }
        }

        Button(
            onClick = {
                viewModel.saveEquipment(selectedEquipment)
                viewModel.completeOnboarding()
                onComplete()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
        ) {
            Text("Finish Setup", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun EquipmentItem(
    name: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onToggle,
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.small),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            // Checkbox has internal padding
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

package com.sjn.gym.feature.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EquipmentSelectionScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val equipmentList = listOf(
        "Dumbbells", "Barbell", "Kettlebell", "Pull-up Bar",
        "Cable Machine", "Leg Press", "Smith Machine", "Squat Rack",
        "Bench", "Medicine Ball", "Resistance Bands"
    )

    var selectedEquipment by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "What equipment do you have?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { selectedEquipment = emptySet() },
                modifier = Modifier.weight(1f)
            ) {
                Text("None")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = { selectedEquipment = equipmentList.toSet() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Full Gym")
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(equipmentList) { equipment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedEquipment = if (selectedEquipment.contains(equipment)) {
                                selectedEquipment - equipment
                            } else {
                                selectedEquipment + equipment
                            }
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedEquipment.contains(equipment),
                        onCheckedChange = { isChecked ->
                            selectedEquipment = if (isChecked) {
                                selectedEquipment + equipment
                            } else {
                                selectedEquipment - equipment
                            }
                        }
                    )
                    Text(
                        text = equipment,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.saveEquipment(selectedEquipment)
                viewModel.completeOnboarding()
                onComplete()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Finish Setup")
        }
    }
}

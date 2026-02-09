package com.sjn.gymexe.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.launch

@Composable
fun EquipmentSettingsScreen(
    // In a real app, inject ViewModel. Using Repo directly for brevity in this specific task context
    // but ideally should be ViewModel.
    // Let's rely on the fact that we can grab the Repo via Hilt if we make a ViewModel.
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val plates by viewModel.availablePlates.collectAsState(initial = emptyList())
    val dumbbells by viewModel.availableDumbbells.collectAsState(initial = emptyList())

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Equipment Settings", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Plates Section
            WeightListEditor(
                title = "Available Plates",
                weights = plates,
                onAddWeight = { viewModel.addPlate(it) },
                onRemoveWeight = { viewModel.removePlate(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Dumbbells Section
            WeightListEditor(
                title = "Available Dumbbells",
                weights = dumbbells,
                onAddWeight = { viewModel.addDumbbell(it) },
                onRemoveWeight = { viewModel.removeDumbbell(it) }
            )
        }
    }
}

@Composable
fun WeightListEditor(
    title: String,
    weights: List<Float>,
    onAddWeight: (Float) -> Unit,
    onRemoveWeight: (Float) -> Unit
) {
    var newWeightInput by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newWeightInput,
                    onValueChange = { newWeightInput = it },
                    label = { Text("Add Weight") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val w = newWeightInput.toFloatOrNull()
                        if (w != null && w > 0) {
                            onAddWeight(w)
                            newWeightInput = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(weights.sorted()) { weight ->
                    InputChip(
                        selected = true,
                        onClick = { },
                        label = { Text(if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString()) },
                        trailingIcon = {
                            IconButton(
                                onClick = { onRemoveWeight(weight) },
                                modifier = Modifier.height(16.dp).width(16.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.height(12.dp))
                            }
                        }
                    )
                }
            }
        }
    }
}

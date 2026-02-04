package com.gym.exe.ui.screens.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gym.exe.ui.components.RestTimerOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val activeProgram by viewModel.activeProgram.collectAsState()
    val currentSets by viewModel.currentSets.collectAsState()

    // Simple state for inputting a set
    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }
    var selectedExerciseId by remember { mutableStateOf("chest_bench_press_barbell") } // Default for demo

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Active Workout", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

                if (activeProgram == null) {
                    Text("No active program found. Please create one.")
                } else {
                    Text("Program: ${activeProgram?.name}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input Section
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Log Set for Bench Press")
                        Row {
                            OutlinedTextField(
                                value = weightInput,
                                onValueChange = { weightInput = it },
                                label = { Text("Weight (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = repsInput,
                                onValueChange = { repsInput = it },
                                label = { Text("Reps") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Button(
                            onClick = {
                                val w = weightInput.toFloatOrNull() ?: 0f
                                val r = repsInput.toIntOrNull() ?: 0
                                viewModel.logSet(selectedExerciseId, w, r, null)
                                repsInput = "" // Clear reps, keep weight
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Log Set")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // History List
                LazyColumn {
                    items(currentSets) { set ->
                        Text("Set: ${set.weight}kg x ${set.reps} reps")
                    }
                }
            }

            // Timer Overlay
            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                val timerState by viewModel.restTimerState.collectAsState()
                RestTimerOverlay(remainingMillis = timerState)
            }
        }
    }
}

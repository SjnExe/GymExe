package com.sjn.gymexe.ui.screens.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.domain.logic.MathInputParser
import com.sjn.gymexe.ui.components.RestTimerOverlay
import com.sjn.gymexe.ui.screens.workout.components.SmartInputRow

@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel(),
) {
    val activeProgram by viewModel.activeProgram.collectAsState()
    val currentSets by viewModel.currentSets.collectAsState()
    val timerState by viewModel.restTimerState.collectAsState()

    val availablePlates by viewModel.availablePlates.collectAsState()
    val availableDumbbells by viewModel.availableDumbbells.collectAsState()

    var showFinishDialog by remember { mutableStateOf(false) }

    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }

    // Defaulting to bench press for demo purposes
    val selectedExerciseId by remember { mutableStateOf("bench_press_barbell") }
    // In a real implementation, this would come from the selected exercise entity
    val selectedEquipmentCategory by remember { mutableStateOf("BARBELL") }
    val machineIncrement: Float? = null
    val machineMax: Float? = null

    val parser = remember { MathInputParser() }

    if (showFinishDialog) {
        FinishWorkoutDialog(
            onConfirm = {
                viewModel.finishWorkout()
                showFinishDialog = false
                navController.popBackStack()
            },
            onDismiss = { showFinishDialog = false },
        )
    }

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                WorkoutHeader(
                    programName = activeProgram?.name,
                    onFinishClick = { showFinishDialog = true },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Log Set: Bench Press", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        SmartInputRow(
                            weightInput = weightInput,
                            onWeightInputChange = { weightInput = it },
                            repsInput = repsInput,
                            onRepsInputChange = { repsInput = it },
                            onLogSet = {
                                val w = parser.parse(weightInput).toFloat()
                                val r = repsInput.toIntOrNull() ?: 0
                                if (r > 0) {
                                    viewModel.logSet(selectedExerciseId, w, r, null, weightInput)
                                    repsInput = ""
                                    // Keep weight for next set convenience
                                }
                            },
                            parser = parser,
                            availablePlates = availablePlates,
                            availableDumbbells = availableDumbbells,
                            equipmentCategory = selectedEquipmentCategory,
                            machineIncrement = machineIncrement,
                            machineMax = machineMax
                        )

                        Button(
                            onClick = {
                                val w = parser.parse(weightInput).toFloat()
                                val r = repsInput.toIntOrNull() ?: 0
                                if (r > 0) {
                                    viewModel.logSet(selectedExerciseId, w, r, null, weightInput)
                                    repsInput = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        ) {
                            Text("Log Set")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SessionHistoryList(currentSets)
            }

            // Timer Overlay
            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                RestTimerOverlay(remainingMillis = timerState)
            }
        }
    }
}

@Composable
private fun FinishWorkoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Finish Workout?") },
        text = { Text("This will save your session and clear current sets.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Finish") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun WorkoutHeader(
    programName: String?,
    onFinishClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Active Workout", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = programName ?: "Quick Workout",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Button(onClick = onFinishClick) {
            Text("Finish")
        }
    }
}

@Composable
private fun SessionHistoryList(currentSets: List<SetEntity>) {
    Text("Current Session", style = MaterialTheme.typography.titleSmall)
    LazyColumn {
        items(currentSets) { set ->
            Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("${set.weight}kg x ${set.reps}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

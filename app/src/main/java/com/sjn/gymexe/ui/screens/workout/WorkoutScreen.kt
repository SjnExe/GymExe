package com.sjn.gymexe.ui.screens.workout

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.ui.components.RestTimerOverlay

@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel(),
) {
    val activeProgram by viewModel.activeProgram.collectAsState()
    val currentSets by viewModel.currentSets.collectAsState()
    val timerState by viewModel.restTimerState.collectAsState()

    var showFinishDialog by remember { mutableStateOf(false) }

    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }
    // Defaulting to bench press for demo purposes
    val selectedExerciseId by remember { mutableStateOf("chest_bench_press_barbell") }

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

                LogSetCard(
                    weightInput = weightInput,
                    onWeightChange = { weightInput = it },
                    repsInput = repsInput,
                    onRepsChange = { repsInput = it },
                    onLogClick = {
                        val w = weightInput.toFloatOrNull() ?: 0f
                        val r = repsInput.toIntOrNull() ?: 0
                        if (r > 0) {
                            viewModel.logSet(selectedExerciseId, w, r, null)
                            repsInput = "" // Clear reps for convenience
                        }
                    },
                )

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
private fun LogSetCard(
    weightInput: String,
    onWeightChange: (String) -> Unit,
    repsInput: String,
    onRepsChange: (String) -> Unit,
    onLogClick: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Log Set: Bench Press", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = onWeightChange,
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = repsInput,
                    onValueChange = onRepsChange,
                    label = { Text("Reps") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = onLogClick,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) {
                Text("Log Set")
            }
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

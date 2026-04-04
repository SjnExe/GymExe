package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    exerciseId: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseDetailsViewModel = hiltViewModel(),
) {
    val exercise by viewModel.exercise.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Exercise Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (exercise != null) {
                Text(
                    text = exercise!!.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(16.dp))

                val severityColor =
                    when (exercise!!.muscleAffectionSeverity) {
                        1 -> Color(0xFF4CAF50) // Low - Green
                        2 -> Color(0xFFFF9800) // Medium - Orange
                        3 -> Color(0xFFF44336) // High - Red
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                Text(
                    text = "Target Muscle: ${exercise!!.targetMuscle}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = severityColor,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Equipment: ${exercise!!.equipment}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Instructions", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = exercise!!.instructions.ifBlank { "No instructions available." },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Placeholder for History Chart / Performance tracking
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Performance History (Coming soon)",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Placeholder for Exercise Settings / Defaults
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Default Settings (Coming soon)",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            } else {
                Text("Loading exercise details...")
            }
        }
    }
}

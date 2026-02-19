package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToExerciseList: () -> Unit,
    onNavigateToWorkout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GymExe") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Today's Workout: Push Day (Placeholder)")
            Button(onClick = onNavigateToWorkout) {
                Text("Start Workout")
            }

            Text("Quick Actions")
            Button(onClick = onNavigateToExerciseList) {
                Text("All Exercises")
            }
            Button(onClick = onNavigateToProfile) {
                Text("Profile & Stats")
            }
        }
    }
}

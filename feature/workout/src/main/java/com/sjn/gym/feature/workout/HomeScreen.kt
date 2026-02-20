package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToExerciseList: () -> Unit,
    onNavigateToWorkout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GymExe") }
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
        }
    }
}

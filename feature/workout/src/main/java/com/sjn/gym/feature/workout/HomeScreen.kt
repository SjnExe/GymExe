package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Today's Workout", style = MaterialTheme.typography.titleMedium)
                    Text("Push Day (Placeholder)", style = MaterialTheme.typography.headlineSmall)
                    Button(
                        onClick = onNavigateToWorkout,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Start Workout")
                    }
                }
            }

            Text("Quick Actions", style = MaterialTheme.typography.titleMedium)

            Button(
                onClick = onNavigateToExerciseList,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Browse Exercises")
            }
        }
    }
}

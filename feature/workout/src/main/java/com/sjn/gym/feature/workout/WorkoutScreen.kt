package com.sjn.gym.feature.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Log Set")
        TextField(
            value = viewModel.input,
            onValueChange = { viewModel.onInputChange(it) },
            label = { Text("Enter Plates (e.g. 2x20)") }
        )

        Row {
            viewModel.plateResult.forEach { plate ->
                Text("${plate.count} x ${plate.weight}kg  ")
            }
        }
    }
}

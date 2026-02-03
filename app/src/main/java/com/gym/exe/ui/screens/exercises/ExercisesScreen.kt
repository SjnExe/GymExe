package com.gym.exe.ui.screens.exercises

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ExercisesScreen(
    navController: NavController,
    viewModel: ExercisesViewModel = hiltViewModel(),
) {
    val exercises by viewModel.exercises.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(exercises) { exercise ->
            Card(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

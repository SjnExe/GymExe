package com.sjn.gymexe.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sjn.gymexe.ui.navigation.Screen

@Composable
fun DashboardScreen(navController: NavController) {
    Column {
        Text(text = "Dashboard")
        Button(onClick = { navController.navigate(Screen.Workout.route) }) {
            Text("Start Workout")
        }
        Button(onClick = { navController.navigate(Screen.Settings.route) }) {
            Text("Settings")
        }
        Button(onClick = { navController.navigate(Screen.Exercises.route) }) {
            Text("Exercises")
        }
    }
}

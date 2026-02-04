package com.sjn.gymexe.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sjn.gymexe.ui.navigation.Screen
import com.sjn.gymexe.ui.screens.dashboard.DashboardScreen
import com.sjn.gymexe.ui.screens.exercises.ExercisesScreen
import com.sjn.gymexe.ui.screens.settings.SettingsScreen
import com.sjn.gymexe.ui.screens.workout.WorkoutScreen

@Composable
fun GymExeAppContent(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(Screen.Workout.route) {
                WorkoutScreen(navController)
            }
            composable(Screen.Exercises.route) {
                ExercisesScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
        }
    }
}

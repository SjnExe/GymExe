package com.gym.exe.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gym.exe.ui.navigation.Screen
import com.gym.exe.ui.screens.dashboard.DashboardScreen
import com.gym.exe.ui.screens.exercises.ExercisesScreen
import com.gym.exe.ui.screens.settings.SettingsScreen
import com.gym.exe.ui.screens.workout.WorkoutScreen

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

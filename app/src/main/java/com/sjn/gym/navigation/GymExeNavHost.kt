package com.sjn.gym.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Settings

@Serializable
object Profile

@Serializable
object Workout

@Serializable
object ExerciseList

@Serializable
object Onboarding

@Composable
fun GymExeNavHost(
    isOnboardingCompleted: Boolean
) {
    val navController = rememberNavController()
    val startDestination: Any = if (isOnboardingCompleted) Home else Onboarding

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Onboarding> {
            com.sjn.gym.feature.onboarding.OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Home) {
                        popUpTo(Onboarding) { inclusive = true }
                    }
                }
            )
        }
        composable<Home> {
            com.sjn.gym.feature.workout.HomeScreen(
                onNavigateToSettings = { navController.navigate(Settings) },
                onNavigateToProfile = { navController.navigate(Profile) },
                onNavigateToExerciseList = { navController.navigate(ExerciseList) },
                onNavigateToWorkout = { navController.navigate(Workout) }
            )
        }
        composable<Workout> {
            com.sjn.gym.feature.workout.WorkoutScreen()
        }
        composable<ExerciseList> {
            com.sjn.gym.feature.workout.ExerciseListScreen()
        }
        composable<Settings> {
            com.sjn.gym.feature.settings.SettingsScreen()
        }
        composable<Profile> {
            com.sjn.gym.feature.profile.ProfileScreen()
        }
    }
}

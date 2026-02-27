package com.sjn.gym.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chuckerteam.chucker.api.Chucker
import com.sjn.gym.BuildConfig
import com.sjn.gym.R
import com.sjn.gym.feature.onboarding.OnboardingScreen
import com.sjn.gym.feature.profile.ProfileScreen
import com.sjn.gym.feature.settings.SettingsScreen
import com.sjn.gym.feature.workout.ExerciseListScreen
import com.sjn.gym.feature.workout.HomeScreen
import com.sjn.gym.feature.workout.LibraryScreen
import com.sjn.gym.feature.workout.WorkoutScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Settings

@Serializable
object Profile

@Serializable
data class Workout(
    val exerciseId: String? = null,
)

@Serializable
object ExerciseList

@Serializable
object Library

@Serializable
object Onboarding

enum class TopLevelDestination(
    val route: Any,
    val icon: ImageVector,
    val labelRes: Int,
) {
    HOME(Home, Icons.Filled.Home, R.string.home),
    LIBRARY(Library, Icons.Filled.LocalLibrary, R.string.library),
    YOU(Profile, Icons.Filled.Person, R.string.you),
}

@Composable
fun GymExeNavHost(isOnboardingCompleted: Boolean) {
    val navController = rememberNavController()
    val startDestination: Any = if (isOnboardingCompleted) Home else Onboarding

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar =
        TopLevelDestination.entries.any { topLevelRoute ->
            currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true
        }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { topLevelRoute ->
                        val isSelected =
                            currentDestination?.hierarchy?.any {
                                it.hasRoute(topLevelRoute.route::class)
                            } == true

                        NavigationBarItem(
                            icon = { Icon(topLevelRoute.icon, contentDescription = stringResource(topLevelRoute.labelRes)) },
                            label = { Text(stringResource(topLevelRoute.labelRes)) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Onboarding> {
                OnboardingScreen(
                    onOnboardingComplete = {
                        navController.navigate(Home) {
                            popUpTo(Onboarding) { inclusive = true }
                        }
                    },
                )
            }
            composable<Home> {
                HomeScreen(
                    onNavigateToExerciseList = { navController.navigate(ExerciseList) },
                    onNavigateToWorkout = { navController.navigate(Workout(null)) },
                )
            }
            composable<Workout> {
                // ViewModel will retrieve arguments via SavedStateHandle
                WorkoutScreen()
            }
            composable<Library> {
                LibraryScreen(
                    onNavigateToWorkout = { exerciseId ->
                        navController.navigate(Workout(exerciseId))
                    },
                )
            }
            composable<ExerciseList> {
                ExerciseListScreen(
                    onExerciseClick = { exerciseId ->
                        navController.navigate(Workout(exerciseId))
                    },
                )
            }
            composable<Settings> {
                val context = LocalContext.current
                SettingsScreen(
                    onNavigateUp = { navController.navigateUp() },
                    isDevMode = BuildConfig.FLAVOR == "dev",
                    onLaunchNetworkInspector = {
                        context.startActivity(Chucker.getLaunchIntent(context))
                    },
                )
            }
            composable<Profile> {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Settings) },
                )
            }
        }
    }
}

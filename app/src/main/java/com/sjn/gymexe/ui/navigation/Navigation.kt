package com.sjn.gymexe.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sjn.gymexe.ui.settings.SettingsScreen

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    data object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)

    data object Workout : Screen("workout", "Workout", Icons.Default.Edit)

    data object Exercises : Screen("exercises", "Exercises", Icons.Default.List)

    data object History : Screen("history", "History", Icons.Default.Person)

    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens =
        listOf(
            Screen.Dashboard,
            Screen.Workout,
            Screen.Exercises,
            Screen.History,
            Screen.Settings,
        )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
        },
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Dashboard.route, builder = {
            composable(Screen.Dashboard.route) { Text("Dashboard Placeholder") } // TODO
            composable(Screen.Workout.route) { Text("Workout Placeholder") } // TODO
            composable(Screen.Exercises.route) { Text("Exercises Placeholder") } // TODO
            composable(Screen.History.route) { Text("History Placeholder") } // TODO
            composable(Screen.Settings.route) { SettingsScreen() }
        })
    }
}

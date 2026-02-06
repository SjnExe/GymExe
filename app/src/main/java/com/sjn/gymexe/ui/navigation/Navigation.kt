package com.sjn.gymexe.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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

    data object Exercises : Screen("exercises", "Exercises", Icons.AutoMirrored.Filled.List)

    data object Profile : Screen("profile", "You", Icons.Default.Person)

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
            Screen.Profile,
        )

    Scaffold(
        topBar = {
            // Settings is accessed via Top Bar, not Bottom Bar
            // Only show on main tabs? For now, show everywhere for simplicity.
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Icon(Screen.Settings.icon, contentDescription = "Settings")
                }
                Text("GymExe", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(48.dp)) // Balance the layout (icon size approx)
            }
        },
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
            composable(Screen.Dashboard.route) {
                PlaceholderScreen("Dashboard", innerPadding.calculateBottomPadding())
            }
            composable(Screen.Workout.route) {
                PlaceholderScreen("Workout", innerPadding.calculateBottomPadding())
            }
            composable(Screen.Exercises.route) {
                PlaceholderScreen("Exercises", innerPadding.calculateBottomPadding())
            }
            composable(Screen.Profile.route) {
                PlaceholderScreen("You / Profile", innerPadding.calculateBottomPadding())
            }
            composable(Screen.Settings.route) { SettingsScreen() }
        })
    }
}

@Composable
fun PlaceholderScreen(
    text: String,
    bottomPadding: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}

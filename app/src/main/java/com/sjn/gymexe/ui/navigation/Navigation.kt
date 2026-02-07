package com.sjn.gymexe.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sjn.gymexe.ui.screens.profile.ProfileScreen
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showNav = currentDestination?.route != Screen.Settings.route
    val showTopBar = currentDestination?.route != Screen.Settings.route

    val context = LocalContext.current
    val activity = context.findActivity()
    // Fallback to Compact if activity not found (shouldn't happen in normal app run)
    val windowSizeClass = if (activity != null) calculateWindowSizeClass(activity) else null
    val widthSizeClass = windowSizeClass?.widthSizeClass ?: WindowWidthSizeClass.Compact

    val config = LocalConfiguration.current
    // Use Navigation Rail if Width is NOT Compact (Medium/Expanded)
    // OR if Height is very small (Split Screen Top/Bottom / Landscape) but Width is decent
    val useNavRail = widthSizeClass != WindowWidthSizeClass.Compact ||
                     (config.screenHeightDp < 480 && config.screenWidthDp >= 480)

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            if (showTopBar) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("GymExe", style = MaterialTheme.typography.titleLarge)
                }
            }
        },
        bottomBar = {
            if (showNav && !useNavRail) {
                NavigationBar {
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
            }
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showNav && useNavRail) {
                NavigationRail {
                    screens.forEach { screen ->
                        NavigationRailItem(
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
            }

            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                composable(Screen.Dashboard.route) {
                    PlaceholderScreen("Dashboard")
                }
                composable(Screen.Workout.route) {
                    PlaceholderScreen("Workout")
                }
                composable(Screen.Exercises.route) {
                    PlaceholderScreen("Exercises")
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController)
                }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(
    text: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

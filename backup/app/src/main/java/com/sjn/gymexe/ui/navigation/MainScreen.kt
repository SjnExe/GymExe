package com.sjn.gymexe.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sjn.gymexe.ui.screens.history.HistoryScreen
import com.sjn.gymexe.ui.screens.profile.ProfileScreen
import com.sjn.gymexe.ui.screens.settings.EquipmentSettingsScreen
import com.sjn.gymexe.ui.screens.workout.WorkoutScreen
import com.sjn.gymexe.ui.settings.SettingsScreen

private const val MIN_WIDTH_TABLET_DP = 480

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

    data object EquipmentSettings : Screen("equipment_settings", "Equipment", Icons.Default.Settings)
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
    val showNav = currentDestination?.route !in listOf(Screen.Settings.route, Screen.EquipmentSettings.route)
    val showTopBar = currentDestination?.route !in listOf(Screen.Settings.route, Screen.EquipmentSettings.route)

    val context = LocalContext.current
    val activity = context.findActivity()
    val windowSizeClass = if (activity != null) calculateWindowSizeClass(activity) else null
    val config = LocalConfiguration.current

    val useNavRail = shouldUseNavRail(windowSizeClass, config)

    if (useNavRail) {
        MainScreenWithRail(
            navController = navController,
            screens = screens,
            showNav = showNav,
            showTopBar = showTopBar,
            currentDestination = currentDestination
        )
    } else {
        MainScreenWithBottomBar(
            navController = navController,
            screens = screens,
            showNav = showNav,
            showTopBar = showTopBar,
            currentDestination = currentDestination
        )
    }
}

private fun shouldUseNavRail(windowSizeClass: WindowSizeClass?, config: Configuration): Boolean {
    val widthSizeClass = windowSizeClass?.widthSizeClass ?: WindowWidthSizeClass.Compact
    return widthSizeClass != WindowWidthSizeClass.Compact ||
            (config.screenHeightDp < MIN_WIDTH_TABLET_DP && config.screenWidthDp >= MIN_WIDTH_TABLET_DP)
}

@Composable
private fun MainScreenWithRail(
    navController: NavHostController,
    screens: List<Screen>,
    showNav: Boolean,
    showTopBar: Boolean,
    currentDestination: NavDestination?
) {
    Row(modifier = Modifier.fillMaxSize()) {
        if (showNav) {
            NavigationRail {
                Spacer(Modifier.weight(1f))
                screens.forEach { screen ->
                    NavigationRailItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = { navController.navigateToScreen(screen.route) },
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = { if (showTopBar) GymExeTopBar(navController) }
        ) { innerPadding ->
            GymExeNavHost(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun MainScreenWithBottomBar(
    navController: NavHostController,
    screens: List<Screen>,
    showNav: Boolean,
    showTopBar: Boolean,
    currentDestination: NavDestination?
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { if (showTopBar) GymExeTopBar(navController) },
        bottomBar = {
            if (showNav) {
                NavigationBar {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = { navController.navigateToScreen(screen.route) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        GymExeNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun GymExeTopBar(navController: NavHostController) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("GymExe", style = MaterialTheme.typography.titleLarge)

        // Settings Button
        IconButton(onClick = { navController.navigate(Screen.EquipmentSettings.route) }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun GymExeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            // Dashboard can be history summary for now
            HistoryScreen()
        }
        composable(Screen.Workout.route) {
            WorkoutScreen(navController)
        }
        composable(Screen.Exercises.route) {
            PlaceholderScreen("Exercises Library")
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.EquipmentSettings.route) { EquipmentSettingsScreen() }
    }
}

private fun NavHostController.navigateToScreen(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateToScreen.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
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

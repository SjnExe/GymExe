package com.gym.exe.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")

    object Workout : Screen("workout")

    object Exercises : Screen("exercises")

    object Settings : Screen("settings")
}

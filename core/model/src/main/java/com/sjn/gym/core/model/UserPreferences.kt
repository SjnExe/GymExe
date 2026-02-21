package com.sjn.gym.core.model

data class UserPreferences(
    val isOnboardingCompleted: Boolean,
    val themeMode: String, // "SYSTEM", "LIGHT", "DARK"
    val useDynamicColor: Boolean
)

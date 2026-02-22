package com.sjn.gym.core.model.backup

data class RestoreOptions(
    val restoreExercises: Boolean = true,
    val restoreWorkouts: Boolean = true,
    val restoreProfile: Boolean = true,
    val restoreSettings: Boolean = true,
)

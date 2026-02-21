package com.sjn.gym.core.model.backup

import kotlinx.serialization.Serializable

/**
 * Data structure for backup/restore.
 * This represents the entire contents of a .gym backup file.
 */
@Serializable
data class GymBackupData(
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val exercises: List<BackupExercise> = emptyList(),
    val workouts: List<BackupWorkout> = emptyList(),
    val profile: BackupProfile? = null,
    val settings: BackupSettings? = null
)

@Serializable
data class BackupExercise(
    val id: String, // Or Int, depending on your DB. Assuming String (UUID) for now or convert from Int
    val name: String,
    val description: String?,
    val muscleGroup: String?,
    val equipment: String?
)

@Serializable
data class BackupWorkout(
    val id: String,
    val date: Long,
    val name: String?,
    val exercises: List<BackupWorkoutExercise> = emptyList()
)

@Serializable
data class BackupWorkoutExercise(
    val exerciseId: String,
    val sets: List<BackupSet> = emptyList()
)

@Serializable
data class BackupSet(
    val reps: Int,
    val weight: Double,
    val unit: String // "kg" or "lbs"
)

@Serializable
data class BackupProfile(
    val name: String?,
    val age: Int?,
    val weight: Double?,
    val height: Double?,
    val gender: String?
)

@Serializable
data class BackupSettings(
    val themeMode: String, // "LIGHT", "DARK", "SYSTEM"
    val weightUnit: String, // "KG", "LBS"
    val heightUnit: String // "CM", "FT"
)

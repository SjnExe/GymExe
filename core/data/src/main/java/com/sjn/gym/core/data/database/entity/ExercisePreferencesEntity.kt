package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_preferences")
data class ExercisePreferencesEntity(
    @PrimaryKey val exerciseId: String,
    val defaultRestTimeSeconds: Int = 90,
    val defaultSets: Int = 3,
    val defaultReps: Int = 10,
    val defaultWeight: Double = 0.0,
)

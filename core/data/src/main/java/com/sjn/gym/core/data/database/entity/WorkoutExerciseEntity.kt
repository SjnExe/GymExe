package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_exercises")
data class WorkoutExerciseEntity(
    @PrimaryKey val id: String,
    val workoutSessionId: String,
    val exerciseId: Long,
    val orderIndex: Int,
    val note: String = "",
)

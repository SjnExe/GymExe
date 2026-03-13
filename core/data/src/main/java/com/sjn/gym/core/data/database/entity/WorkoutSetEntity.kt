package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sjn.gym.core.model.SetType

@Entity(tableName = "workout_sets")
data class WorkoutSetEntity(
    @PrimaryKey val id: String,
    val workoutExerciseId: String,
    val orderIndex: Int,
    val type: SetType,
    val weight: Double?,
    val reps: Int?,
    val timeSeconds: Int?,
    val distance: Double?,
    val rpe: Int?,
    val isCompleted: Boolean,
)

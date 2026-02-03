package com.gym.exe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.gym.exe.data.local.entity.ExerciseEntity
import com.gym.exe.data.local.entity.WorkoutExerciseEntity

data class WorkoutExerciseWithDetails(
    @Embedded val workoutExercise: WorkoutExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id",
    )
    val exercise: ExerciseEntity,
)

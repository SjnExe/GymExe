package com.gym.exe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.gym.exe.data.local.entity.WorkoutEntity
import com.gym.exe.data.local.entity.WorkoutExerciseEntity

data class WorkoutWithExercises(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id", // WorkoutEntity.id
        entityColumn = "workoutId", // WorkoutExerciseEntity.workoutId
        entity = WorkoutExerciseEntity::class
    )
    val exercises: List<WorkoutExerciseWithDetails>
)

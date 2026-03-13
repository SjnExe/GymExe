package com.sjn.gym.core.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithExercises(
    @Embedded val routine: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId",
        entity = RoutineExerciseEntity::class,
    )
    val exercises: List<RoutineExerciseEntity>,
)

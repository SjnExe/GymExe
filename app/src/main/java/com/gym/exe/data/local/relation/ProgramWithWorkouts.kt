package com.gym.exe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.gym.exe.data.local.entity.ProgramEntity
import com.gym.exe.data.local.entity.WorkoutEntity

data class ProgramWithWorkouts(
    @Embedded val program: ProgramEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "programId"
    )
    val workouts: List<WorkoutEntity>
)

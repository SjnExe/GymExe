package com.sjn.gymexe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.entity.WorkoutEntity

data class ProgramWithWorkouts(
    @Embedded val program: ProgramEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "programId",
    )
    val workouts: List<WorkoutEntity>,
)

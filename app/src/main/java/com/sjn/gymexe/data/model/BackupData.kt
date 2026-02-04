package com.sjn.gymexe.data.model

import com.sjn.gymexe.data.local.entity.ExerciseEntity
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.entity.SessionEntity
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.data.local.entity.WorkoutEntity
import com.sjn.gymexe.data.local.entity.WorkoutExerciseEntity
import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = 1,
    val timestamp: Long,
    val exercises: List<ExerciseEntity>,
    val programs: List<ProgramEntity>,
    val workouts: List<WorkoutEntity>,
    val workoutExercises: List<WorkoutExerciseEntity>,
    val sessions: List<SessionEntity>,
    val sets: List<SetEntity>,
)

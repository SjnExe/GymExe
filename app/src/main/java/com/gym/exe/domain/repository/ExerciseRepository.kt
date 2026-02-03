package com.gym.exe.domain.repository

import com.gym.exe.data.local.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    suspend fun getExerciseById(id: String): ExerciseEntity?

    suspend fun syncExercisesFromAssets()

    suspend fun addCustomExercise(exercise: ExerciseEntity)
}

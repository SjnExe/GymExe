package com.sjn.gymexe.data.repository

import com.sjn.gymexe.domain.repository.ExerciseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {
    override suspend fun syncExercisesFromAssets() {
        // Placeholder implementation
    }
}

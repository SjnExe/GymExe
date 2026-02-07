package com.sjn.gymexe.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun syncExercisesFromAssets()
    // Add other methods as needed later
}

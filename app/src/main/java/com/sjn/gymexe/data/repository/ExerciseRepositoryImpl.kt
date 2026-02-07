package com.sjn.gymexe.data.repository

import android.content.Context
import com.sjn.gymexe.domain.repository.ExerciseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExerciseRepository {
    override suspend fun syncExercisesFromAssets() {
        // Placeholder implementation
    }
}

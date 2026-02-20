package com.sjn.gymexe.domain.repository

import com.sjn.gymexe.domain.model.ExerciseStats
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getExerciseStats(exerciseId: String): Flow<ExerciseStats>
}

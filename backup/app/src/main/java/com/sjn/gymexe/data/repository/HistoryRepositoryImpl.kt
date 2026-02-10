package com.sjn.gymexe.data.repository

import com.sjn.gymexe.data.local.dao.SetDao
import com.sjn.gymexe.domain.model.ExerciseStats
import com.sjn.gymexe.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val setDao: SetDao
) : HistoryRepository {
    override fun getExerciseStats(exerciseId: String): Flow<ExerciseStats> = flow {
        // Fetch Last Used
        val lastSet = setDao.getLastSetForExercise(exerciseId)
        val lastWeight = lastSet?.weight

        // Fetch PR
        val pr = setDao.getPersonalRecordForExercise(exerciseId)

        emit(ExerciseStats(lastUsedWeight = lastWeight, personalBest = pr))
    }
}

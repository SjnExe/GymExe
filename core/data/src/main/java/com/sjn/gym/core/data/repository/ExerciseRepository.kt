package com.sjn.gym.core.data.repository

import com.sjn.gym.core.data.database.dao.ExerciseDao
import com.sjn.gym.core.data.database.entity.ExerciseEntity
import com.sjn.gym.core.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository
    @Inject
    constructor(
        private val exerciseDao: ExerciseDao,
    ) {
        fun getAllExercises(): Flow<List<Exercise>> =
            exerciseDao.getAll().map { entities ->
                entities.map { it.toDomainModel() }
            }

        private fun ExerciseEntity.toDomainModel(): Exercise =
            Exercise(
                id = id.toString(),
                name = name,
                bodyPart = bodyPart,
                targetMuscle = targetMuscle,
                secondaryMuscles =
                    if (secondaryMuscles.isEmpty()) {
                        emptyList()
                    } else {
                        secondaryMuscles.split(",").map { it.trim() }
                    },
                equipment = equipment,
                type = type,
                isCustom = isCustom,
            )
    }

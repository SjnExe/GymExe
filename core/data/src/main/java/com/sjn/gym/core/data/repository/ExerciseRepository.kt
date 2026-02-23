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

        suspend fun getExercise(id: String): Exercise? {
            val longId = id.toLongOrNull() ?: return null
            return exerciseDao.getById(longId)?.toDomainModel()
        }

        private fun ExerciseEntity.toDomainModel(): Exercise {
            // "None" or "Bodyweight" might be equipment values
            val equipmentValue = if (equipment.isBlank()) "None" else equipment

            return Exercise(
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
                equipment = equipmentValue,
                type = type,
                isCustom = isCustom,
            )
        }
    }

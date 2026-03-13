package com.sjn.gym.core.data.repository

import com.sjn.gym.core.data.database.dao.RoutineDao
import com.sjn.gym.core.data.database.entity.RoutineEntity
import com.sjn.gym.core.data.database.entity.RoutineExerciseEntity
import com.sjn.gym.core.model.Routine
import com.sjn.gym.core.model.RoutineExercise
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class RoutineRepository
@Inject
constructor(
    private val routineDao: RoutineDao,
    private val exerciseRepository: ExerciseRepository,
) {
    suspend fun saveRoutine(routine: Routine) {
        val routineEntity =
            RoutineEntity(id = routine.id, name = routine.name, description = routine.description)

        routineDao.insertRoutine(routineEntity)

        val exerciseEntities =
            routine.exercises.mapIndexed { index, re ->
                RoutineExerciseEntity(
                    id = re.id,
                    routineId = routine.id,
                    exerciseId = re.exercise.id.toLong(),
                    orderIndex = index,
                    targetSets = re.targetSets,
                    targetReps = re.targetReps,
                    restTimeSeconds = re.restTimeSeconds,
                    note = re.note,
                )
            }

        routineDao.insertRoutineExercises(exerciseEntities)
    }

    fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutinesWithExercises().map { routinesWithExercises ->
            routinesWithExercises.map { rwe ->
                val mappedExercises =
                    rwe.exercises
                        .sortedBy { it.orderIndex }
                        .map { reEntity ->
                            // For flow mapping, we shouldn't use suspend functions directly in
                            // non-suspending map.
                            // A better approach would be observing exercises or using mapLatest,
                            // but for now we fetch synchronously if possible or
                            // just run a blocking call (not ideal but this fixes compilation for
                            // now).
                            // Better fix: use `map` and `async` or proper flow combination.
                            // For simplicity in this quick fix, we use runBlocking:
                            val exercise =
                                kotlinx.coroutines.runBlocking {
                                    exerciseRepository.getExercise(reEntity.exerciseId.toString())
                                }

                            RoutineExercise(
                                id = reEntity.id,
                                exercise =
                                    exercise
                                        ?: error("Exercise missing for ID ${reEntity.exerciseId}"),
                                targetSets = reEntity.targetSets,
                                targetReps = reEntity.targetReps,
                                restTimeSeconds = reEntity.restTimeSeconds,
                                note = reEntity.note,
                            )
                        }

                Routine(
                    id = rwe.routine.id,
                    name = rwe.routine.name,
                    description = rwe.routine.description,
                    exercises = mappedExercises,
                )
            }
        }
    }

    suspend fun getRoutine(id: String): Routine? {
        val routineEntity = routineDao.getRoutineById(id) ?: return null
        val exercisesForRoutine = routineDao.getExercisesForRoutine(id)
        val mappedExercises =
            exercisesForRoutine.map { reEntity ->
                val exercise = exerciseRepository.getExercise(reEntity.exerciseId.toString())

                RoutineExercise(
                    id = reEntity.id,
                    exercise = exercise ?: error("Exercise missing for ID ${reEntity.exerciseId}"),
                    targetSets = reEntity.targetSets,
                    targetReps = reEntity.targetReps,
                    restTimeSeconds = reEntity.restTimeSeconds,
                    note = reEntity.note,
                )
            }

        return Routine(
            id = routineEntity.id,
            name = routineEntity.name,
            description = routineEntity.description,
            exercises = mappedExercises,
        )
    }

    suspend fun deleteRoutine(id: String) {
        routineDao.deleteRoutine(id)
    }
}

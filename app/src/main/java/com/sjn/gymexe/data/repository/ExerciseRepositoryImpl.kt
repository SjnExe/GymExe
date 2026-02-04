package com.sjn.gymexe.data.repository

import android.content.Context
import android.util.Log
import com.sjn.gymexe.data.local.dao.ExerciseDao
import com.sjn.gymexe.data.local.entity.ExerciseEntity
import com.sjn.gymexe.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class ExerciseRepositoryImpl(
    private val context: Context,
    private val exerciseDao: ExerciseDao,
    // Default or injected if modified constructor
    private val json: Json = Json { ignoreUnknownKeys = true },
) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()

    override suspend fun getExerciseById(id: String): ExerciseEntity? = exerciseDao.getExerciseById(id)

    @Suppress("TooGenericExceptionCaught")
    override suspend fun syncExercisesFromAssets() {
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("exercises.json")
                val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

                val exercises = json.decodeFromString<List<ExerciseEntity>>(jsonString)

                // Merge logic: Upsert all. Official exercises have fixed IDs, so they update.
                exerciseDao.insertExercises(exercises)
            } catch (e: Exception) {
                Log.e("ExerciseRepository", "Error syncing assets", e)
            }
        }
    }

    override suspend fun addCustomExercise(exercise: ExerciseEntity) {
        val customExercise = exercise.copy(isCustom = true)
        exerciseDao.insertExercise(customExercise)
    }
}

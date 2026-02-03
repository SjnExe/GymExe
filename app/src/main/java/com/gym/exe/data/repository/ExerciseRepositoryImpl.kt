package com.gym.exe.data.repository

import android.content.Context
import com.gym.exe.data.local.dao.ExerciseDao
import com.gym.exe.data.local.entity.ExerciseEntity
import com.gym.exe.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class ExerciseRepositoryImpl(
    private val context: Context,
    private val exerciseDao: ExerciseDao,
    private val json: Json = Json { ignoreUnknownKeys = true } // Default or injected if modified constructor
) : ExerciseRepository {

    override fun getAllExercises(): Flow<List<ExerciseEntity>> {
        return exerciseDao.getAllExercises()
    }

    override suspend fun getExerciseById(id: String): ExerciseEntity? {
        return exerciseDao.getExerciseById(id)
    }

    override suspend fun syncExercisesFromAssets() {
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("exercises.json")
                val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

                val exercises = json.decodeFromString<List<ExerciseEntity>>(jsonString)

                // Merge logic: Upsert all. Official exercises have fixed IDs, so they update.
                exerciseDao.insertExercises(exercises)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun addCustomExercise(exercise: ExerciseEntity) {
        val customExercise = exercise.copy(isCustom = true)
        exerciseDao.insertExercise(customExercise)
    }
}

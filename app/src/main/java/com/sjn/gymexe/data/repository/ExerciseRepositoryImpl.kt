package com.sjn.gymexe.data.repository

import android.content.Context
import com.sjn.gymexe.data.local.dao.ExerciseDao
import com.sjn.gymexe.data.local.entity.ExerciseEntity
import com.sjn.gymexe.domain.repository.ExerciseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exerciseDao: ExerciseDao,
) : ExerciseRepository {

    override suspend fun syncExercisesFromAssets() {
        try {
            val jsonString = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
            val exercises = Json.decodeFromString<List<ExerciseEntity>>(jsonString)

            // Insert or Update
            // Strategy: We want to update official exercises if their properties changed,
            // but we shouldn't overwrite user custom exercises (which have different IDs anyway).
            // However, if we simply insert on conflict replace, we might lose user customizations
            // if we ever allow editing official exercises (which we probably shouldn't).
            // For now, Replace is safe as long as IDs are unique.

            exerciseDao.insertExercises(exercises)

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle error (log it)
        } catch (e: Exception) {
             e.printStackTrace()
        }
    }
}

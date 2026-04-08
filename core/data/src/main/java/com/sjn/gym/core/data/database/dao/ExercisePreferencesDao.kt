package com.sjn.gym.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sjn.gym.core.data.database.entity.ExercisePreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisePreferencesDao {
    @Query("SELECT * FROM exercise_preferences WHERE exerciseId = :exerciseId")
    fun getPreferences(exerciseId: String): Flow<ExercisePreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(preferences: ExercisePreferencesEntity)
}

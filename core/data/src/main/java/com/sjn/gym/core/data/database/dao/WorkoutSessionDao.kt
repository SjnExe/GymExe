package com.sjn.gym.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sjn.gym.core.data.database.entity.WorkoutExerciseEntity
import com.sjn.gym.core.data.database.entity.WorkoutSessionEntity
import com.sjn.gym.core.data.database.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<WorkoutExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<WorkoutSetEntity>)

    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>

    @Query(
        "SELECT * FROM workout_exercises WHERE workoutSessionId = :sessionId ORDER BY orderIndex ASC"
    )
    suspend fun getExercisesForSession(sessionId: String): List<WorkoutExerciseEntity>

    @Query(
        "SELECT * FROM workout_sets WHERE workoutExerciseId = :exerciseId ORDER BY orderIndex ASC"
    )
    suspend fun getSetsForExercise(exerciseId: String): List<WorkoutSetEntity>
}

package com.sjn.gymexe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.entity.WorkoutEntity
import com.sjn.gymexe.data.local.entity.WorkoutExerciseEntity
import com.sjn.gymexe.data.local.relation.ProgramWithWorkouts
import com.sjn.gymexe.data.local.relation.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs")
    @JvmSuppressWildcards
    suspend fun getAllProgramsList(): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE id = :id")
    @JvmSuppressWildcards
    suspend fun getProgramById(id: Long): ProgramEntity?

    @Query("SELECT * FROM workouts")
    @JvmSuppressWildcards
    suspend fun getAllWorkoutsList(): List<WorkoutEntity>

    @Query("SELECT * FROM workout_exercises")
    @JvmSuppressWildcards
    suspend fun getAllWorkoutExercisesList(): List<WorkoutExerciseEntity>

    @Query("SELECT * FROM programs WHERE isActive = 1 LIMIT 1")
    fun getActiveProgram(): Flow<ProgramEntity?>

    @Transaction
    @Query("SELECT * FROM programs WHERE id = :programId")
    fun getProgramWithWorkouts(programId: Long): Flow<ProgramWithWorkouts>

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): Flow<WorkoutWithExercises>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertProgram(program: ProgramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long

    @Query("UPDATE programs SET isActive = 0")
    @JvmSuppressWildcards
    suspend fun clearActivePrograms(): Int

    @Query("UPDATE programs SET isActive = 1 WHERE id = :programId")
    @JvmSuppressWildcards
    suspend fun setActiveProgram(programId: Long): Int
}

package com.gym.exe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gym.exe.data.local.entity.ProgramEntity
import com.gym.exe.data.local.entity.WorkoutEntity
import com.gym.exe.data.local.entity.WorkoutExerciseEntity
import com.gym.exe.data.local.relation.ProgramWithWorkouts
import com.gym.exe.data.local.relation.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs")
    suspend fun getAllProgramsList(): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE id = :id")
    suspend fun getProgramById(id: Long): ProgramEntity?

    @Query("SELECT * FROM workouts")
    suspend fun getAllWorkoutsList(): List<WorkoutEntity>

    @Query("SELECT * FROM workout_exercises")
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
    suspend fun insertProgram(program: ProgramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Query("UPDATE programs SET isActive = 0")
    suspend fun clearActivePrograms()

    @Query("UPDATE programs SET isActive = 1 WHERE id = :programId")
    suspend fun setActiveProgram(programId: Long)
}

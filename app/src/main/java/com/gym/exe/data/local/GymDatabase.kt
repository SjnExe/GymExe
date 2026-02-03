package com.gym.exe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gym.exe.data.local.dao.ExerciseDao
import com.gym.exe.data.local.dao.ProgramDao
import com.gym.exe.data.local.dao.SessionDao
import com.gym.exe.data.local.dao.SetDao
import com.gym.exe.data.local.entity.ExerciseEntity
import com.gym.exe.data.local.entity.ProgramEntity
import com.gym.exe.data.local.entity.SessionEntity
import com.gym.exe.data.local.entity.SetEntity
import com.gym.exe.data.local.entity.WorkoutEntity
import com.gym.exe.data.local.entity.WorkoutExerciseEntity

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        SessionEntity::class,
        SetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GymDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programDao(): ProgramDao
    abstract fun sessionDao(): SessionDao
    abstract fun setDao(): SetDao
}

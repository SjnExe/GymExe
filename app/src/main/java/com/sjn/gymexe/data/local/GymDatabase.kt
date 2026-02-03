package com.sjn.gymexe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sjn.gymexe.data.local.dao.ExerciseDao
import com.sjn.gymexe.data.local.dao.ProgramDao
import com.sjn.gymexe.data.local.dao.SessionDao
import com.sjn.gymexe.data.local.dao.SetDao
import com.sjn.gymexe.data.local.entity.ExerciseEntity
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.entity.SessionEntity
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.data.local.entity.WorkoutEntity
import com.sjn.gymexe.data.local.entity.WorkoutExerciseEntity

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        SessionEntity::class,
        SetEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class GymDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    abstract fun programDao(): ProgramDao

    abstract fun sessionDao(): SessionDao

    abstract fun setDao(): SetDao
}

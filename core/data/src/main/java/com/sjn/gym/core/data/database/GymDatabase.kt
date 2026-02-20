package com.sjn.gym.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sjn.gym.core.data.database.converters.Converters
import com.sjn.gym.core.data.database.dao.ExerciseDao
import com.sjn.gym.core.data.database.entity.ExerciseEntity

@Database(entities = [ExerciseEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}

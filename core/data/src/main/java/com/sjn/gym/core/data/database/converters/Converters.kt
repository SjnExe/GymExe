package com.sjn.gym.core.data.database.converters

import androidx.room.TypeConverter
import com.sjn.gym.core.model.ExerciseType

class Converters {
    @TypeConverter
    fun fromExerciseType(value: ExerciseType): String = value.name

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType = ExerciseType.valueOf(value)
}

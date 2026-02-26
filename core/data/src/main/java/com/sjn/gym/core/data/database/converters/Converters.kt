package com.sjn.gym.core.data.database.converters

import androidx.room.TypeConverter
import com.sjn.gym.core.model.ExerciseType

class Converters {
    @TypeConverter
    fun fromExerciseType(value: ExerciseType): String = value.name

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType =
        try {
            ExerciseType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            // Fallback for unknown/legacy types to prevent crash
            // If "REPS_ONLY" was missing, we added it back, but this safety is crucial.
            ExerciseType.WEIGHT_REPS
        }
}

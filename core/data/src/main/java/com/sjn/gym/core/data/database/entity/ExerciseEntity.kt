package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sjn.gym.core.model.ExerciseType

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val bodyPart: String,
    val targetMuscle: String,
    val secondaryMuscles: String = "", // Comma separated
    val equipment: String,
    val type: ExerciseType,
    val isCustom: Boolean,
)

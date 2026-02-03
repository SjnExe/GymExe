package com.gym.exe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "exercises")
@Serializable
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val target: String,
    // Strength, Cardio
    val type: String,
    val equipment: String,
    val isCustom: Boolean = false,
)

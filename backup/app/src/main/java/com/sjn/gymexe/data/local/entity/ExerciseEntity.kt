package com.sjn.gymexe.data.local.entity

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
    // Human-readable equipment name (e.g., "Olympic Barbell")
    val equipment: String,
    // Logic category: BARBELL, DUMBBELL, MACHINE, CABLE, BODYWEIGHT
    val equipmentCategory: String = "BARBELL",
    val isCustom: Boolean = false,
    // For Machines/Cables: The default increment step (e.g., 5.0)
    val machineIncrement: Float? = null,
    // For Machines/Cables: The max stack weight (e.g., 80.0)
    val machineMax: Float? = null,
)

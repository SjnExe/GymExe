package com.sjn.gym.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_days")
data class RoutineDayEntity(
    @PrimaryKey val id: String,
    val routineId: String,
    val dayName: String, // e.g., "Day 1" (Repeat) or "Monday" (Fixed)
    val dayIndex: Int, // 0-based index for sorting
    val isRestDay: Boolean = false,
    val universalRestTimeSeconds: Int = 90,
)

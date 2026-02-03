package com.gym.exe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "sessions")
@Serializable
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutName: String, // Snapshot name
    val startTime: Long,
    val endTime: Long? = null,
    val note: String? = null
)

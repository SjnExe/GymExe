package com.sjn.gymexe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "sessions")
@Serializable
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Snapshot name
    val workoutName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val note: String? = null,
)

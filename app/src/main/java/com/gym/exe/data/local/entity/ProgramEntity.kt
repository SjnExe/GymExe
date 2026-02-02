package com.gym.exe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "programs")
@Serializable
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // "Weekly", "Rolling"
    val isActive: Boolean = false,
    val currentSplitIndex: Int = 0 // For rolling split tracking
)

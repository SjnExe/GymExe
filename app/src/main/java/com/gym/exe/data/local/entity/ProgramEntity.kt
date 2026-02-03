package com.gym.exe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "programs")
@Serializable
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    // "Weekly", "Rolling"
    val type: String,
    val isActive: Boolean = false,
    // For rolling split tracking
    val currentSplitIndex: Int = 0,
)

package com.gym.exe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
@Serializable
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programId: Long,
    // "Leg Day"
    val name: String,
    // 1-7 for Weekly, Null for Rolling
    val dayOfWeek: Int? = null,
    // Sequence for Rolling
    val orderIndex: Int,
)

package com.sjn.gymexe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("sessionId"), Index("exerciseId")],
)
@Serializable
data class SetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: String,
    val weight: Float,
    val reps: Int,
    val rpe: Float? = null,
    val timestamp: Long,
    val isDropSet: Boolean = false,
    val completed: Boolean = true,
    // Duration in seconds (for timed exercises)
    val duration: Long? = null,
    // Distance in meters (for cardio)
    val distance: Float? = null,
    // Raw input expression (e.g., "20 10 5")
    val inputString: String? = null,
)

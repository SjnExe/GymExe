package com.sjn.gym.core.model

import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSession(
    val id: String,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class) val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val endTime: LocalDateTime? = null,
    val exercises: List<WorkoutExercise> = emptyList(),
)

@Serializable
data class WorkoutExercise(
    val id: String,
    val exercise: Exercise,
    val sets: List<WorkoutSet> = emptyList(),
    val note: String = "",
)

@Serializable
data class WorkoutSet(
    val id: String,
    val type: SetType = SetType.NORMAL,
    val weight: Double? = null,
    val reps: Int? = null,
    val timeSeconds: Int? = null,
    val distance: Double? = null,
    val rpe: Int? = null,
    val isCompleted: Boolean = false,
)

enum class SetType {
    WARMUP,
    NORMAL,
    FAILURE,
    DROP_SET,
}

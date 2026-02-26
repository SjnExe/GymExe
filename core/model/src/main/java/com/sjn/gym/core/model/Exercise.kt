package com.sjn.gym.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val targetMuscle: String,
    val secondaryMuscles: List<String> = emptyList(),
    val equipment: String, // "Barbell", "Dumbbell", "Machine", "None"
    val type: ExerciseType = ExerciseType.WEIGHT_REPS,
    val isCustom: Boolean = false,
)

enum class ExerciseType {
    WEIGHT_REPS, // Standard
    REPS_ONLY, // Bodyweight (Pullups, Pushups)
    TIME, // Plank
    CARDIO, // Distance/Time/Speed
}

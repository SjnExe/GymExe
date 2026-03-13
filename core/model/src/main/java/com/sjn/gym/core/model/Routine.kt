package com.sjn.gym.core.model

data class Routine(
    val id: String,
    val name: String,
    val description: String = "",
    val exercises: List<RoutineExercise> = emptyList(),
)

data class RoutineExercise(
    val id: String,
    val exercise: Exercise,
    val targetSets: Int,
    val targetReps: Int?,
    val restTimeSeconds: Int?,
    val note: String = "",
)

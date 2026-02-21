package com.sjn.gym.core.model

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val gender: Gender,
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
}

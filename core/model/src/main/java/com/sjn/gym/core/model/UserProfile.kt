package com.sjn.gym.core.model

data class UserProfile(
    val id: String,
    val name: String,
    val birthDate: Long,
    val weight: Double,
    val height: Double,
    val gender: Gender,
    val profilePictureUri: String? = null,
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
}

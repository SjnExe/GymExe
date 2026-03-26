package com.sjn.gym.core.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class UserProfileTest {

    @Test
    fun `user profile initializes correctly with provided values`() {
        val userProfile =
            UserProfile(
                id = "user123",
                name = "John Doe",
                birthDate = 946684800000L, // Jan 1, 2000
                weight = 80.5,
                height = 180.0,
                gender = Gender.MALE,
                profilePictureUri = "content://images/profile.jpg",
            )

        assertEquals("user123", userProfile.id)
        assertEquals("John Doe", userProfile.name)
        assertEquals(946684800000L, userProfile.birthDate)
        assertEquals(80.5, userProfile.weight)
        assertEquals(180.0, userProfile.height)
        assertEquals(Gender.MALE, userProfile.gender)
        assertEquals("content://images/profile.jpg", userProfile.profilePictureUri)
    }

    @Test
    fun `user profile defaults to null profile picture`() {
        val userProfile =
            UserProfile(
                id = "user456",
                name = "Jane Doe",
                birthDate = 946684800000L,
                weight = 65.0,
                height = 165.0,
                gender = Gender.FEMALE,
            )

        assertNull(userProfile.profilePictureUri)
    }
}

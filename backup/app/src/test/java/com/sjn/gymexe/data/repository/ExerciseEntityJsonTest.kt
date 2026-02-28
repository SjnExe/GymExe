package com.sjn.gymexe.data.repository

import com.sjn.gymexe.data.local.entity.ExerciseEntity
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExerciseEntityJsonTest {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Test
    fun `exercises_json parses correctly`() {
        // Mock JSON string based on assets/exercises.json
        val jsonString = """
            [
              {
                "id": "squat_barbell",
                "name": "Squat",
                "target": "Legs",
                "type": "Strength",
                "equipment": "Barbell",
                "equipmentCategory": "BARBELL",
                "isCustom": false
              },
              {
                "id": "cable_tricep_pushdown",
                "name": "Tricep Pushdown",
                "target": "Triceps",
                "type": "Strength",
                "equipment": "Cable",
                "equipmentCategory": "CABLE",
                "isCustom": false,
                "machineIncrement": 5.0,
                "machineMax": 80.0
              }
            ]
        """.trimIndent()

        val exercises = json.decodeFromString<List<ExerciseEntity>>(jsonString)

        assertEquals(2, exercises.size)
        assertEquals("squat_barbell", exercises[0].id)
        assertEquals("BARBELL", exercises[0].equipmentCategory)
        assertEquals(5.0f, exercises[1].machineIncrement)
    }
}

package com.sjn.gym.feature.workout.logic

import javax.inject.Inject

sealed interface WeightParsingResult {
    data class Success(
        val weights: List<Double>,
    ) : WeightParsingResult

    data class Failure(
        val message: String,
    ) : WeightParsingResult
}

class WeightInputParser
    @Inject
    constructor() {
        fun parse(
            input: String,
            equipment: String,
        ): WeightParsingResult {
            if (input.isBlank()) return WeightParsingResult.Success(emptyList())

            // Common normalization
            val normalizedInput =
                input
                    .trim()
                    .replace("+", " ")
                    .replace("*", "x")
                    .replace("×", "x")
                    .replace("\\s+".toRegex(), " ") // Normalize multiple spaces to single space

            val isStackable = isStackableEquipment(equipment)

            if (!isStackable) {
                // Strict Mode: Single number only.
                // Allowed: "20", "20.5"
                // Disallowed: "20 20", "2x10", "10+10"

                // Check for any separators that indicate multiple values
                if (normalizedInput.contains(" ") || normalizedInput.contains("x", ignoreCase = true)) {
                    return WeightParsingResult.Failure("Stacking weights is not allowed for $equipment.")
                }

                val weight = normalizedInput.toDoubleOrNull()
                return if (weight != null) {
                    WeightParsingResult.Success(listOf(weight))
                } else {
                    WeightParsingResult.Failure("Invalid number format.")
                }
            } else {
                // Stackable Mode (Barbell): Allow space, x, +
                val tokens = normalizedInput.split(" ")
                val weights = mutableListOf<Double>()

                for (token in tokens) {
                    if (token.contains("x", ignoreCase = true)) {
                        val parts = token.split("x", ignoreCase = true)
                        if (parts.size == 2) {
                            val countStr = parts[0]
                            val weightStr = parts[1]

                            val count = countStr.toIntOrNull()
                            val weight = weightStr.toDoubleOrNull()

                            if (count != null && weight != null) {
                                repeat(count) { weights.add(weight) }
                            } else {
                                // Edge case: "x5" or "5x" or invalid numbers
                                return WeightParsingResult.Failure("Invalid format: $token")
                            }
                        } else {
                            return WeightParsingResult.Failure("Invalid format: $token")
                        }
                    } else {
                        val weight = token.toDoubleOrNull()
                        if (weight != null) {
                            weights.add(weight)
                        } else {
                            return WeightParsingResult.Failure("Invalid format: $token")
                        }
                    }
                }
                return WeightParsingResult.Success(weights)
            }
        }

        private fun isStackableEquipment(equipment: String): Boolean =
            when (equipment.lowercase()) {
                "barbell" -> true

                // "plate loaded machine" - if we had this data
                else -> false
            }
    }

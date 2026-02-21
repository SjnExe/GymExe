package com.sjn.gym.feature.workout.logic

import javax.inject.Inject

data class PlateCount(
    val weight: Double,
    val count: Int,
)

class PlateCalculator
    @Inject
    constructor() {
        @Suppress("NestedBlockDepth")
        fun parse(input: String): List<PlateCount> {
            val tokens =
                input
                    .replace("+", " ")
                    .replace("*", "x")
                    .replace("×", "x")
                    .split(" ")
                    .filter { it.isNotBlank() }

            val plates = mutableListOf<PlateCount>()

            for (token in tokens) {
                if (token.contains("x", ignoreCase = true)) {
                    val parts = token.split("x", ignoreCase = true)
                    if (parts.size == 2) {
                        val count = parts[0].toIntOrNull()
                        val weight = parts[1].toDoubleOrNull()
                        if (count != null && weight != null) {
                            plates.add(PlateCount(weight, count))
                        }
                    }
                } else {
                    val weight = token.toDoubleOrNull()
                    if (weight != null) {
                        plates.add(PlateCount(weight, 1))
                    }
                }
            }

            // Aggregate same weights
            return plates
                .groupBy { it.weight }
                .map { (weight, list) -> PlateCount(weight, list.sumOf { it.count }) }
                .sortedBy { it.weight }
        }

        fun calculateTotalWeight(
            input: String,
            barWeight: Double = 0.0,
        ): Double {
            val plates = parse(input)
            return barWeight + plates.sumOf { it.weight * it.count }
        }
    }

package com.sjn.gymexe.domain.logic

import javax.inject.Inject

class MathInputParser @Inject constructor() {

    fun parse(input: String): Double {
        if (input.isBlank()) return 0.0

        // 1. Sanitize: Replace 'x', 'X', '*' with '*' for multiplication
        //    Replace spaces with '+'
        val sanitized = input
            .replace(Regex("[xX×]"), "*")
            .replace(Regex("\\s+"), "+")

        return try {
            // Simple recursive descent or expression evaluation
            // For "20+2*10+5", standard precedence applies (* before +)
            evaluateExpression(sanitized)
        } catch (e: Exception) {
            0.0
        }
    }

    private fun evaluateExpression(expression: String): Double {
        // Split by '+' first
        val terms = expression.split("+")
        var sum = 0.0

        for (term in terms) {
            if (term.contains("*")) {
                // Handle multiplication
                val factors = term.split("*")
                var product = 1.0
                for (factor in factors) {
                    product *= factor.toDoubleOrNull() ?: 1.0 // Default to 1 if parse fails? Or 0?
                    // "2* *5" -> 2 * 1 * 5 = 10. Acceptable for now.
                }
                sum += product
            } else {
                // Just a number
                sum += term.toDoubleOrNull() ?: 0.0
            }
        }
        return sum
    }

    /**
     * Intellegently appends a weight to the current input string.
     * Logic:
     * - "20" + 10 -> "20 10"
     * - "20 10" + 10 -> "20 2x10"
     * - "20 2x10" + 10 -> "20 3x10"
     * - "20 5" + 10 -> "20 5 10"
     */
    fun smartAppend(currentInput: String, weightToAdd: Float): String {
        val weightStr = weightToAdd.formatSmart()
        val parts = currentInput.trim().split(Regex("\\s+")).toMutableList()

        if (parts.isEmpty() || currentInput.isBlank()) {
            return weightStr
        }

        val lastPart = parts.last()

        // Check if last part is just the number (e.g. "10")
        if (lastPart == weightStr) {
            // Change "10" to "2x10"
            parts[parts.lastIndex] = "2x$weightStr"
            return parts.joinToString(" ")
        }

        // Check if last part is already a multiplication (e.g. "2x10")
        // Regex to capture "2" and "10" from "2x10"
        val match = Regex("(\\d+)x($weightStr)").matchEntire(lastPart)
        if (match != null) {
            val (countStr, _) = match.destructured
            val count = countStr.toIntOrNull() ?: 1
            // Increment count
            parts[parts.lastIndex] = "${count + 1}x$weightStr"
            return parts.joinToString(" ")
        }

        // Default: just append
        return "$currentInput $weightStr"
    }

    private fun Float.formatSmart(): String {
        return if (this % 1.0 == 0.0) {
            this.toInt().toString()
        } else {
            this.toString()
        }
    }
}

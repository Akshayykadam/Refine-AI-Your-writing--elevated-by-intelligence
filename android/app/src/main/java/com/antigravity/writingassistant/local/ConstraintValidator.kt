package com.antigravity.writingassistant.local

object ConstraintValidator {

    private const val MAX_INPUT_LENGTH = 400
    private const val MAX_OUTPUT_RATIO = 1.2

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val reason: String) : ValidationResult()
    }

    fun validateInput(text: String): ValidationResult {
        if (text.isBlank()) {
            return ValidationResult.Invalid("Input is empty.")
        }
        
        if (text.length > MAX_INPUT_LENGTH) {
            return ValidationResult.Invalid("Text too long for local rewrite. Max $MAX_INPUT_LENGTH characters.")
        }
        
        // Check for lists (bullet points or numbered lists)
        if (text.contains("\n- ") || text.contains("\n* ") || text.matches(Regex(".*\\n\\d+\\..*", RegexOption.DOT_MATCHES_ALL))) {
            return ValidationResult.Invalid("Lists are not supported in local mode.")
        }
        
        // Check for multiple paragraphs (more than 1 double newline)
        // We allow single newlines within sentences, but double newlines imply paragraphs.
        val paragraphs = text.split("\n\n")
        if (paragraphs.size > 1) {
             return ValidationResult.Invalid("Single paragraph only supported locally.")
        }

        return ValidationResult.Valid
    }
}

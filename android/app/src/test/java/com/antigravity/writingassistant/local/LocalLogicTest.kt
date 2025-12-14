package com.antigravity.writingassistant.local

import org.junit.Test
import org.junit.Assert.*

class LocalLogicTest {

    // --- ConstraintValidator Tests ---

    @Test
    fun `Validator accepts short simple sentence`() {
        val input = "This is a simple sentence."
        val result = ConstraintValidator.validateInput(input)
        assertTrue(result is ConstraintValidator.ValidationResult.Valid)
    }

    @Test
    fun `Validator rejects empty input`() {
        val input = ""
        val result = ConstraintValidator.validateInput(input)
        assertTrue(result is ConstraintValidator.ValidationResult.Invalid)
        assertEquals("Input is empty.", (result as ConstraintValidator.ValidationResult.Invalid).reason)
    }

    @Test
    fun `Validator rejects input longer than 400 chars`() {
        val longInput = "a".repeat(401)
        val result = ConstraintValidator.validateInput(longInput)
        assertTrue(result is ConstraintValidator.ValidationResult.Invalid)
        assertEquals("Text too long for local rewrite. Max 400 characters.", (result as ConstraintValidator.ValidationResult.Invalid).reason)
    }

    @Test
    fun `Validator rejects bullet lists`() {
        val input = "Here is a list:\n- Item 1\n- Item 2"
        val result = ConstraintValidator.validateInput(input)
        assertTrue(result is ConstraintValidator.ValidationResult.Invalid)
        assertEquals("Lists are not supported in local mode.", (result as ConstraintValidator.ValidationResult.Invalid).reason)
    }

    @Test
    fun `Validator rejects numbered lists`() {
        val input = "Here is a list:\n1. Item 1\n2. Item 2"
        val result = ConstraintValidator.validateInput(input)
        assertTrue(result is ConstraintValidator.ValidationResult.Invalid)
        assertEquals("Lists are not supported in local mode.", (result as ConstraintValidator.ValidationResult.Invalid).reason)
    }

    @Test
    fun `Validator rejects multiple paragraphs`() {
        val input = "First paragraph.\n\nSecond paragraph."
        val result = ConstraintValidator.validateInput(input)
        assertTrue(result is ConstraintValidator.ValidationResult.Invalid)
        assertEquals("Single paragraph only supported locally.", (result as ConstraintValidator.ValidationResult.Invalid).reason)
    }

    // --- LocalPromptBuilder Tests (Gemma Format) ---

    @Test
    fun `PromptBuilder outputs Gemma chat template`() {
        val input = "Hello world"
        val prompt = LocalPromptBuilder.buildPrompt(input, "Refine")
        assertTrue("Prompt should contain Gemma start tag", prompt.contains("<start_of_turn>user"))
        assertTrue("Prompt should contain input text", prompt.contains("Text: Hello world"))
        assertTrue("Prompt should end with model turn", prompt.contains("<start_of_turn>model"))
    }

    @Test
    fun `PromptBuilder includes formal instruction`() {
        val prompt = LocalPromptBuilder.buildPrompt("Test", "Professional")
        assertTrue(prompt.contains("formal, professional tone"))
    }

    @Test
    fun `PromptBuilder includes love instruction`() {
        val prompt = LocalPromptBuilder.buildPrompt("Test", "Love")
        assertTrue(prompt.contains("affectionate, loving tone"))
    }

    @Test
    fun `PromptBuilder includes grammar instruction`() {
        val prompt = LocalPromptBuilder.buildPrompt("Test", "Grammar")
        assertTrue(prompt.contains("Fix all grammar and spelling errors"))
    }

    // --- LocalPromptBuilder.getTaskType Tests ---

    @Test
    fun `getTaskType returns Professional`() {
        assertEquals("Professional", LocalPromptBuilder.getTaskType("Make it professional"))
    }

    @Test
    fun `getTaskType returns Love`() {
        assertEquals("Love", LocalPromptBuilder.getTaskType("Love"))
    }

    @Test
    fun `getTaskType returns Refine for unknown`() {
        assertEquals("Refine", LocalPromptBuilder.getTaskType("Random instruction"))
    }
}

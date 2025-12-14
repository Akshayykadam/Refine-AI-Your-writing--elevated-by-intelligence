package com.antigravity.writingassistant.local

object LocalPromptBuilder {

    /**
     * Builds a Gemma-compatible prompt using its chat template format.
     * Format: <start_of_turn>user\n{instruction}\n<end_of_turn>\n<start_of_turn>model\n
     */
    fun buildPrompt(input: String, instruction: String): String {
        val taskDescription = when {
            instruction.contains("professional", ignoreCase = true) -> 
                "Rewrite the following text in a formal, professional tone. Return only the rewritten text."
            instruction.contains("formal", ignoreCase = true) -> 
                "Rewrite the following text in a formal tone. Return only the rewritten text."
            instruction.contains("casual", ignoreCase = true) -> 
                "Rewrite the following text in a casual, friendly tone. Return only the rewritten text."
            instruction.contains("warm", ignoreCase = true) -> 
                "Rewrite the following text in a warm, caring tone. Return only the rewritten text."
            instruction.contains("love", ignoreCase = true) -> 
                "Rewrite the following text in an affectionate, loving tone. Return only the rewritten text."
            instruction.contains("concise", ignoreCase = true) -> 
                "Summarize the following text to be more concise. Return only the summary."
            instruction.contains("grammar", ignoreCase = true) -> 
                "Fix all grammar and spelling errors in the following text. Return only the corrected text."
            instruction.contains("emojify", ignoreCase = true) -> 
                "Add relevant emojis to the following text. Return only the text with emojis."
            else -> 
                "Improve the following text to be clearer and more readable. Return only the improved text."
        }
        
        // Gemma instruction-tuned chat template
        return """<start_of_turn>user
$taskDescription

Text: $input
<end_of_turn>
<start_of_turn>model
"""
    }
    
    /**
     * Returns just the task type for simulation/logging.
     */
    fun getTaskType(instruction: String): String {
        return when {
            instruction.contains("professional", ignoreCase = true) -> "Professional"
            instruction.contains("formal", ignoreCase = true) -> "Formal"
            instruction.contains("casual", ignoreCase = true) -> "Casual"
            instruction.contains("warm", ignoreCase = true) -> "Warm"
            instruction.contains("love", ignoreCase = true) -> "Love"
            instruction.contains("concise", ignoreCase = true) -> "Concise"
            instruction.contains("grammar", ignoreCase = true) -> "Grammar"
            instruction.contains("emojify", ignoreCase = true) -> "Emojify"
            else -> "Refine"
        }
    }
}

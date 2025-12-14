package com.antigravity.writingassistant.local

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import java.io.File

class LocalRewriteEngine(private val context: Context) {

    private var llmInference: LlmInference? = null
    private var isModelLoaded = false
    
    // Model filename - must be pushed to device storage (not assets, too large)
    private val MODEL_FILE = "gemma-2b-it-cpu-int4.bin"
    
    // Default model path on device storage
    private val modelPath: String
        get() = File(context.filesDir, MODEL_FILE).absolutePath

    init {
        checkModelExists()
    }

    private fun checkModelExists() {
        try {
            val modelFile = File(modelPath)
            if (modelFile.exists()) {
                loadModel()
            } else {
                android.util.Log.w("LocalRewriteEngine", "Model file $MODEL_FILE not found at $modelPath. Running in SIMULATION MODE.")
                android.util.Log.i("LocalRewriteEngine", "To enable local AI, push the model: adb push gemma-2b-it-cpu-int4.bin /data/data/${context.packageName}/files/")
                isModelLoaded = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isModelLoaded = false
        }
    }

    private fun loadModel() {
        try {
            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath(modelPath)
                .setMaxTokens(256)
                .setTemperature(0.7f)
                .setTopK(40)
                .build()
            
            llmInference = LlmInference.createFromOptions(context, options)
            isModelLoaded = true
            android.util.Log.i("LocalRewriteEngine", "MediaPipe LLM Loaded Successfully from $modelPath")
        } catch (e: Exception) {
            android.util.Log.e("LocalRewriteEngine", "Failed to load model: ${e.message}")
            e.printStackTrace()
            isModelLoaded = false
        }
    }

    fun rewrite(input: String, instruction: String, callback: (String) -> Unit) {
        // 1. Validate
        val validation = ConstraintValidator.validateInput(input)
        if (validation is ConstraintValidator.ValidationResult.Invalid) {
            callback("[Local Error] ${validation.reason}")
            return
        }

        // 2. Build Prompt
        val prompt = LocalPromptBuilder.buildPrompt(input, instruction)
        val taskType = LocalPromptBuilder.getTaskType(instruction)

        // 3. Inference
        if (isModelLoaded && llmInference != null) {
            runInference(prompt, taskType, callback)
        } else {
            runSimulation(input, taskType, callback)
        }
    }

    private fun runSimulation(input: String, taskType: String, callback: (String) -> Unit) {
        // Simulation delayed response with mock rewrite based on task type
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val result = fakeRewrite(taskType, input)
            callback("[Sim]: $result")
        }, 1200)
    }
    
    private fun fakeRewrite(taskType: String, text: String): String {
        return when (taskType) {
            "Professional", "Formal" -> "Dear recipient, $text"
            "Casual" -> "Hey! $text"
            "Warm" -> "$text (warmly)"
            "Love" -> "My dearest, $text"
            "Concise" -> text.take(50) + if (text.length > 50) "..." else ""
            "Grammar" -> text.replaceFirstChar { it.uppercase() } + "."
            "Emojify" -> "$text [with emojis]"
            else -> text
        }
    }
    
    private fun runInference(prompt: String, taskType: String, callback: (String) -> Unit) {
        Thread {
            try {
                val response = llmInference?.generateResponse(prompt)
                val cleanedResponse = cleanModelOutput(response ?: "")
                
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    if (cleanedResponse.isNotBlank()) {
                        callback(cleanedResponse)
                    } else {
                        callback("[Local Error] Empty response from model.")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("LocalRewriteEngine", "Inference failed: ${e.message}")
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    // Fallback to simulation on error
                    runSimulation(prompt.substringAfter("Text: ").substringBefore("\n"), taskType, callback)
                }
            }
        }.start()
    }
    
    private fun cleanModelOutput(output: String): String {
        // Remove any lingering template tags and trim
        return output
            .replace("<end_of_turn>", "")
            .replace("<start_of_turn>", "")
            .replace("model", "")
            .trim()
    }

    /**
     * Check if model file exists (for UI to show download button)
     */
    fun isModelAvailable(): Boolean {
        return File(modelPath).exists()
    }

    /**
     * Reload model after download completes
     */
    fun reloadModel() {
        close()
        checkModelExists()
    }

    fun close() {
        llmInference?.close()
        llmInference = null
        isModelLoaded = false
    }
}


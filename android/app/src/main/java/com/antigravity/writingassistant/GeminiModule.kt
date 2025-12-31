package com.antigravity.writingassistant

import android.content.ComponentName
import android.provider.Settings
import android.text.TextUtils
import com.facebook.react.bridge.*

class GeminiModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "GeminiModule"

    @ReactMethod
    fun isAccessibilityServiceEnabled(promise: Promise) {
        try {
            val expectedComponentName = ComponentName(reactApplicationContext, WritingAssistService::class.java)
            val enabledServicesSetting = Settings.Secure.getString(
                reactApplicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServicesSetting ?: "")

            while (colonSplitter.hasNext()) {
                val componentNameString = colonSplitter.next()
                val enabledComponent = ComponentName.unflattenFromString(componentNameString)
                if (enabledComponent != null && enabledComponent == expectedComponentName) {
                    promise.resolve(true)
                    return
                }
            }
            promise.resolve(false)
        } catch (e: Exception) {
            promise.resolve(false)
        }

    }

    private val geminiClient = GeminiClient()

    @ReactMethod
    fun generateContent(originalText: String, instruction: String, promise: Promise) {
        val config = GeminiClient.PromptConfig(
            originalText = originalText,
            instruction = instruction
        )
        geminiClient.generateContent(config) { result ->
            if (result != null) {
                promise.resolve(result)
            } else {
                promise.reject("GEMINI_ERROR", "Failed to generate content")
            }
        }
    }

    @ReactMethod
    fun copyToClipboard(text: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            try {
                val clipboard = reactApplicationContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Copied Text", text)
                clipboard.setPrimaryClip(clip)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @ReactMethod
    fun installApk(filePath: String) {
        try {
            val path = if (filePath.startsWith("file://")) filePath.replace("file://", "") else filePath
            val file = java.io.File(path)
            if (file.exists()) {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                val uri = androidx.core.content.FileProvider.getUriForFile(
                    reactApplicationContext,
                    reactApplicationContext.packageName + ".provider",
                    file
                )
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                reactApplicationContext.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // Required for RN event emitter
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Required for RN event emitter
    }
}

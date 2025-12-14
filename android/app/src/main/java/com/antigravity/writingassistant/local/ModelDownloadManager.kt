package com.antigravity.writingassistant.local

import android.content.Context
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Manages downloading the Gemma 2B model file for local inference.
 */
class ModelDownloadManager(private val context: Context) {

    interface DownloadListener {
        fun onProgress(bytesDownloaded: Long, totalBytes: Long, percent: Int)
        fun onComplete(filePath: String)
        fun onError(message: String)
    }

    private val client = OkHttpClient()
    private var currentCall: Call? = null
    
    // Model configuration
    companion object {
        const val MODEL_FILENAME = "gemma-2b-it-cpu-int4.bin"
        
        // TODO: Replace with your hosted model URL
        // Kaggle requires auth, so host the model on your own server/CDN
        const val MODEL_URL = "https://github.com/Akshayykadam/Refine-AI-Your-writing--elevated-by-intelligence/releases/download/v1.0.0-model/gemma-2b-it-cpu-int4.bin"
        
        // Expected file size for validation (~1.5GB)
        const val EXPECTED_SIZE_BYTES = 1_500_000_000L
    }

    val modelPath: String
        get() = File(context.filesDir, MODEL_FILENAME).absolutePath

    fun isModelDownloaded(): Boolean {
        val file = File(modelPath)
        return file.exists() && file.length() > 100_000_000 // At least 100MB
    }

    fun getModelSizeMB(): Long {
        val file = File(modelPath)
        return if (file.exists()) file.length() / (1024 * 1024) else 0
    }

    fun downloadModel(listener: DownloadListener) {
        if (isModelDownloaded()) {
            listener.onComplete(modelPath)
            return
        }

        val request = Request.Builder()
            .url(MODEL_URL)
            .build()

        currentCall = client.newCall(request)
        currentCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!call.isCanceled()) {
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        listener.onError("Download failed: ${e.message}")
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        listener.onError("Server error: ${response.code}")
                    }
                    return
                }

                val body = response.body
                if (body == null) {
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        listener.onError("Empty response from server")
                    }
                    return
                }

                val totalBytes = body.contentLength()
                val tempFile = File(context.filesDir, "$MODEL_FILENAME.tmp")
                val finalFile = File(modelPath)

                try {
                    val inputStream = body.byteStream()
                    val outputStream = FileOutputStream(tempFile)
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    var lastReportedPercent = -1

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        val percent = if (totalBytes > 0) {
                            ((totalBytesRead * 100) / totalBytes).toInt()
                        } else {
                            -1
                        }

                        // Report progress every 1%
                        if (percent != lastReportedPercent) {
                            lastReportedPercent = percent
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                listener.onProgress(totalBytesRead, totalBytes, percent)
                            }
                        }
                    }

                    outputStream.close()
                    inputStream.close()

                    // Rename temp file to final
                    if (tempFile.renameTo(finalFile)) {
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            listener.onComplete(modelPath)
                        }
                    } else {
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            listener.onError("Failed to save model file")
                        }
                    }

                } catch (e: Exception) {
                    tempFile.delete()
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        listener.onError("Write error: ${e.message}")
                    }
                }
            }
        })
    }

    fun cancelDownload() {
        currentCall?.cancel()
        // Clean up temp file
        File(context.filesDir, "$MODEL_FILENAME.tmp").delete()
    }

    fun deleteModel(): Boolean {
        return File(modelPath).delete()
    }
}

package com.sjn.gym.util

import android.content.Context
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileLoggingTree(
    context: Context,
) : Timber.DebugTree() {
    private val logFile: File = File(context.cacheDir, "app_logs.txt")

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        try {
            // Ensure directory exists
            logFile.parentFile?.mkdirs()

            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(Date())
            val priorityString =
                when (priority) {
                    Log.VERBOSE -> "VERBOSE"
                    Log.DEBUG -> "DEBUG"
                    Log.INFO -> "INFO"
                    Log.WARN -> "WARN"
                    Log.ERROR -> "ERROR"
                    Log.ASSERT -> "ASSERT"
                    else -> "UNKNOWN"
                }

            val logMessage = "$timestamp $priorityString/$tag: $message\n"

            // Append to file
            FileWriter(logFile, true).use { writer ->
                writer.append(logMessage)
                t?.printStackTrace(java.io.PrintWriter(writer))
            }
        } catch (e: IOException) {
            Log.e("FileLoggingTree", "Error writing to log file", e)
        }
    }

    fun getLogFile(): File = logFile
}

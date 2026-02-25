package com.sjn.gym.core.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        // Store logs in /Android/data/com.sjn.gym.dev.debug/files/
        // This makes them accessible via USB/MTP on non-rooted devices without special permissions.
        private val logFile: File = File(context.getExternalFilesDir(null), "gymexe_logs.txt")

        fun getLogFile(): File = logFile

        @Synchronized
        fun appendLog(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?,
        ) {
            try {
                // Ensure directory exists
                if (logFile.parentFile?.exists() == false) {
                    logFile.parentFile?.mkdirs()
                }

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

                // Append to file synchronously as logging is often synchronous
                // Use append mode
                FileWriter(logFile, true).use { writer ->
                    writer.append(logMessage)
                    t?.printStackTrace(java.io.PrintWriter(writer))
                }
            } catch (e: IOException) {
                // Fallback to system log if file write fails
                Log.e("LogRepository", "Error writing to log file: ${e.message}")
            }
        }

        suspend fun clearLog() {
            withContext(Dispatchers.IO) {
                if (logFile.exists()) {
                    logFile.delete()
                }
            }
        }

        suspend fun copyLogTo(destination: File) {
            withContext(Dispatchers.IO) {
                if (logFile.exists()) {
                    logFile.copyTo(destination, overwrite = true)
                } else {
                    throw IOException("Log file not found at ${logFile.absolutePath}")
                }
            }
        }
    }

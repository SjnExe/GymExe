package com.sjn.gym

import android.app.Application
import android.content.Intent
import android.os.Process
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import com.sjn.gym.core.data.repository.LogRepository
import com.sjn.gym.ui.crash.CrashActivity
import com.sjn.gym.util.FileLogWriter
import dagger.hilt.android.HiltAndroidApp
import java.io.PrintWriter
import java.io.StringWriter
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltAndroidApp
class GymExeApp : Application() {
    @Inject
    lateinit var logRepository: LogRepository

    override fun onCreate() {
        super.onCreate()

        val writers = mutableListOf<co.touchlab.kermit.LogWriter>()
        if (BuildConfig.DEBUG || BuildConfig.FLAVOR == "dev") {
            writers.add(LogcatWriter())
        }

        // Plant FileLoggingTree for all dev builds (debug and release)
        if (BuildConfig.FLAVOR == "dev") {
            writers.add(FileLogWriter(logRepository))
            Logger.setLogWriters(writers)
            Logger.i { "App started" } // Ensure log file is created

            // Capture uncaught exceptions
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                try {
                    // Log to file
                    Logger.e(throwable) { "Uncaught Exception on thread ${thread.name}" }

                    // Convert stack trace to string
                    val sw = StringWriter()
                    throwable.printStackTrace(PrintWriter(sw))
                    val stackTrace = sw.toString()

                    // Launch CrashActivity
                    val intent =
                        Intent(this, CrashActivity::class.java).apply {
                            putExtra("error_details", stackTrace)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                    startActivity(intent)

                    // Kill the process to avoid system crash dialog and ensure fresh start
                    Process.killProcess(Process.myPid())
                    exitProcess(10)
                } catch (e: Exception) {
                    // If crash handling fails, fall back to default
                    defaultHandler?.uncaughtException(thread, throwable)
                }
            }
        }
    }
}

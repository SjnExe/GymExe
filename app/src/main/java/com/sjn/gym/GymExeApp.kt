package com.sjn.gym

import android.app.Application
import com.sjn.gym.core.data.repository.LogRepository
import com.sjn.gym.util.FileLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GymExeApp : Application() {
    @Inject
    lateinit var logRepository: LogRepository

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (BuildConfig.FLAVOR == "dev") {
            // Ensure logRepository is initialized before use (Hilt does this)
            Timber.plant(FileLoggingTree(logRepository))

            // Capture uncaught exceptions
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                Timber.e(throwable, "Uncaught Exception on thread ${thread.name}")
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }
}

package com.sjn.gym.util

import android.util.Log
import com.sjn.gym.core.data.repository.LogRepository
import timber.log.Timber

class FileLoggingTree(
    private val logRepository: LogRepository,
) : Timber.DebugTree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        logRepository.appendLog(priority, tag, message, t)
    }

    fun getLogFile() = logRepository.getLogFile()
}

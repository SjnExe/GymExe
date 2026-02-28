package com.sjn.gym.util

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import com.sjn.gym.core.data.repository.LogRepository

class FileLogWriter(
    private val logRepository: LogRepository,
) : LogWriter() {
    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?,
    ) {
        val priority =
            when (severity) {
                Severity.Verbose -> android.util.Log.VERBOSE
                Severity.Debug -> android.util.Log.DEBUG
                Severity.Info -> android.util.Log.INFO
                Severity.Warn -> android.util.Log.WARN
                Severity.Error -> android.util.Log.ERROR
                Severity.Assert -> android.util.Log.ASSERT
            }
        logRepository.appendLog(priority, tag, message, throwable)
    }
}

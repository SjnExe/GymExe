package com.gym.exe.domain.manager

import java.io.File

interface UpdateManager {
    suspend fun checkForUpdates(): UpdateResult
    fun downloadUpdate(url: String, fileName: String)
}

sealed class UpdateResult {
    object NoUpdate : UpdateResult()
    data class UpdateAvailable(val version: String, val url: String, val isBeta: Boolean) : UpdateResult()
    data class Error(val e: Exception) : UpdateResult()
}

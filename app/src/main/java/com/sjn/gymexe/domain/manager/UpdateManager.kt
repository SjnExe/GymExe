package com.sjn.gymexe.domain.manager

import kotlinx.coroutines.flow.Flow

interface UpdateManager {
    suspend fun checkForUpdates(): UpdateResult

    fun downloadUpdate(
        url: String,
        fileName: String,
    )

    fun downloadUpdateFlow(
        url: String,
        fileName: String,
    ): Flow<DownloadStatus>
}

sealed class UpdateResult {
    data object NoUpdate : UpdateResult()

    data class UpdateAvailable(
        val version: String,
        val url: String,
        val isBeta: Boolean,
    ) : UpdateResult()

    data class Error(
        val e: Exception,
    ) : UpdateResult()
}

sealed class DownloadStatus {
    data object Idle : DownloadStatus()

    data class Downloading(
        val progress: Float,
    ) : DownloadStatus()

    data class Completed(
        val fileUri: String,
    ) : DownloadStatus()

    data class Error(
        val message: String,
    ) : DownloadStatus()
}

package com.gym.exe.domain.manager

import android.net.Uri

interface BackupManager {
    suspend fun createBackup(folderUri: Uri): Result<String>

    suspend fun restoreBackup(fileUri: Uri): Result<Unit>
}

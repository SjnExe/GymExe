package com.sjn.gym.core.data.repository.backup

import com.sjn.gym.core.model.backup.GymBackupData
import java.io.InputStream
import java.io.OutputStream

/**
 * Repository responsible for backup and restore operations.
 */
interface BackupRepository {
    /**
     * Exports the current database state to the given output stream.
     * The format is JSON.
     */
    suspend fun exportData(outputStream: OutputStream): Result<Unit>

    /**
     * Reads a backup file (JSON) from the input stream and parses it into [GymBackupData].
     */
    suspend fun parseBackup(inputStream: InputStream): Result<GymBackupData>

    /**
     * Restores data from the provided [GymBackupData] object based on the [RestoreOptions].
     */
    suspend fun restoreData(
        data: GymBackupData,
        options: RestoreOptions,
    ): Result<Unit>
}

data class RestoreOptions(
    val restoreExercises: Boolean = true,
    val restoreWorkouts: Boolean = true,
    val restoreProfile: Boolean = true,
    val restoreSettings: Boolean = true,
)

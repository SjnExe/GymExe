package com.sjn.gymexe.data.manager

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.room.withTransaction
import com.sjn.gymexe.data.local.GymDatabase
import com.sjn.gymexe.data.model.BackupData
import com.sjn.gymexe.domain.manager.BackupManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupManagerImpl(
    private val context: Context,
    private val db: GymDatabase,
    private val json: Json,
) : BackupManager {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun createBackup(folderUri: Uri): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                // 1. Fetch Data
                val exercises = db.exerciseDao().getAllExercisesList()
                val programs = db.programDao().getAllProgramsList()
                val workouts = db.programDao().getAllWorkoutsList()
                val workoutExercises = db.programDao().getAllWorkoutExercisesList()
                val sessions = db.sessionDao().getAllSessionsList()
                val sets = db.setDao().getAllSetsList()

                val backupData =
                    BackupData(
                        timestamp = System.currentTimeMillis(),
                        exercises = exercises,
                        programs = programs,
                        workouts = workouts,
                        workoutExercises = workoutExercises,
                        sessions = sessions,
                        sets = sets,
                    )

                val jsonString = json.encodeToString(backupData)

                // 2. Create File
                val dir =
                    DocumentFile.fromTreeUri(context, folderUri)
                        ?: return@withContext Result.failure(Exception("Invalid Directory"))

                val fileName = "GymExe_Backup_${System.currentTimeMillis()}.gymexe"
                val file =
                    dir.createFile("application/zip", fileName)
                        ?: return@withContext Result.failure(Exception("Could not create file"))

                // 3. Write ZIP
                context.contentResolver.openOutputStream(file.uri)?.use { os ->
                    ZipOutputStream(BufferedOutputStream(os)).use { zos ->
                        val entry = ZipEntry("backup.json")
                        zos.putNextEntry(entry)
                        zos.write(jsonString.toByteArray())
                        zos.closeEntry()
                    }
                } ?: return@withContext Result.failure(Exception("Could not open stream"))

                Result.success(fileName)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun restoreBackup(fileUri: Uri): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                var jsonString: String? = null

                // 1. Read ZIP
                context.contentResolver.openInputStream(fileUri)?.use { `is` ->
                    ZipInputStream(`is`).use { zis ->
                        var entry = zis.nextEntry
                        while (entry != null) {
                            if (entry.name == "backup.json") {
                                jsonString = BufferedReader(InputStreamReader(zis)).readText()
                                break
                            }
                            entry = zis.nextEntry
                        }
                    }
                }

                if (jsonString == null) {
                    return@withContext Result.failure(Exception("Invalid backup file: backup.json not found"))
                }

                // 2. Parse
                val data = json.decodeFromString<BackupData>(jsonString!!)

                // 3. Restore to DB
                db.withTransaction {
                    db.clearAllTables()

                    db.exerciseDao().insertExercises(data.exercises)
                    // Programs
                    data.programs.forEach { db.programDao().insertProgram(it) }
                    data.workouts.forEach { db.programDao().insertWorkout(it) }
                    data.workoutExercises.forEach { db.programDao().insertWorkoutExercise(it) }
                    // Sessions
                    data.sessions.forEach { db.sessionDao().insertSession(it) }
                    db.setDao().insertSets(data.sets)
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

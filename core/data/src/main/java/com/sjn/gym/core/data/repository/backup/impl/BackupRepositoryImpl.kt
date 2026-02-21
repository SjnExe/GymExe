package com.sjn.gym.core.data.repository.backup.impl

import com.sjn.gym.core.data.repository.UserPreferencesRepository
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.backup.BackupRepository
import com.sjn.gym.core.data.repository.backup.RestoreOptions
import com.sjn.gym.core.model.backup.BackupProfile
import com.sjn.gym.core.model.backup.BackupSettings
import com.sjn.gym.core.model.backup.GymBackupData
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import com.sjn.gym.core.model.UserPreferences
import com.sjn.gym.core.model.UserProfile
import java.util.UUID

@Singleton
class BackupRepositoryImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userProfileRepository: UserProfileRepository
) : BackupRepository {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun exportData(outputStream: OutputStream): Result<Unit> {
        return runCatching {
            val preferences = userPreferencesRepository.userData.first()
            val profile = userProfileRepository.userProfile.first()

            val backupData = GymBackupData(
                profile = profile?.toBackup(),
                settings = preferences.toBackup()
            )

            val jsonString = json.encodeToString(backupData)

            outputStream.bufferedWriter().use { writer ->
                writer.write(jsonString)
            }
        }
    }

    override suspend fun parseBackup(inputStream: InputStream): Result<GymBackupData> {
        return runCatching {
            inputStream.bufferedReader().use { reader ->
                val jsonString = reader.readText()
                json.decodeFromString<GymBackupData>(jsonString)
            }
        }
    }

    override suspend fun restoreData(data: GymBackupData, options: RestoreOptions): Result<Unit> {
        return runCatching {
            val settings = data.settings
            if (options.restoreSettings && settings != null) {
                userPreferencesRepository.setThemeMode(settings.themeMode)
                // TODO: Add methods to UserPreferencesRepository to set units if not present
                // userPreferencesRepository.setWeightUnit(data.settings.weightUnit)
                // userPreferencesRepository.setHeightUnit(data.settings.heightUnit)
            }

            val profile = data.profile
            if (options.restoreProfile && profile != null) {
                userProfileRepository.saveProfile(profile.toDomain())
            }
        }
    }

    private fun UserProfile.toBackup() = BackupProfile(
        name = name,
        age = age,
        weight = weight,
        height = height,
        gender = gender.name
    )

    private fun BackupProfile.toDomain() = UserProfile(
        id = UUID.randomUUID().toString(), // Profile usually singular, ID might not matter or be overwritten
        name = name ?: "",
        age = age ?: 0,
        weight = weight ?: 0.0,
        height = height ?: 0.0,
        gender = gender?.let {
             try { com.sjn.gym.core.model.Gender.valueOf(it) } catch (e: Exception) { com.sjn.gym.core.model.Gender.MALE }
        } ?: com.sjn.gym.core.model.Gender.MALE
    )

    private fun UserPreferences.toBackup() = BackupSettings(
        themeMode = themeMode,
        weightUnit = "KG", // Placeholder, need to get from preferences if stored
        heightUnit = "CM"  // Placeholder
    )
}

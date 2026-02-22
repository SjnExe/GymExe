package com.sjn.gym.feature.settings

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.backup.BackupRepository
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.core.model.backup.RestoreOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

private const val MOCK_DELAY = 2000L

@HiltViewModel
@Suppress("TooManyFunctions")
class SettingsViewModel
    @Inject
    constructor(
        private val application: Application,
        private val userProfileRepo: UserProfileRepository,
        private val backupRepository: BackupRepository,
    ) : AndroidViewModel(application) {
        val themeConfig =
            userProfileRepo.themeConfig.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                ThemeConfig.SYSTEM,
            )

        val themeStyle =
            userProfileRepo.themeStyle.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                ThemeStyle.DYNAMIC,
            )

        val customThemeColor =
            userProfileRepo.customThemeColor.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                null,
            )

        val weightUnit =
            userProfileRepo.weightUnit.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                WeightUnit.KG,
            )

        val heightUnit =
            userProfileRepo.heightUnit.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                HeightUnit.CM,
            )

        val distanceUnit =
            userProfileRepo.distanceUnit.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                DistanceUnit.KM,
            )

        private val _backupStatus = MutableStateFlow<BackupStatus>(BackupStatus.Idle)
        val backupStatus: StateFlow<BackupStatus> = _backupStatus.asStateFlow()

        private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
        val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

        val appVersion: String = com.sjn.gym.feature.settings.BuildConfig.VERSION_NAME

        fun setThemeConfig(config: ThemeConfig) {
            viewModelScope.launch {
                userProfileRepo.setThemeConfig(config)
            }
        }

        fun setThemeStyle(style: ThemeStyle) {
            viewModelScope.launch {
                userProfileRepo.setThemeStyle(style)
            }
        }

        fun setCustomThemeColor(color: Int) {
            viewModelScope.launch {
                userProfileRepo.setCustomThemeColor(color)
            }
        }

        fun setWeightUnit(unit: WeightUnit) {
            viewModelScope.launch {
                userProfileRepo.setWeightUnit(unit)
            }
        }

        fun setHeightUnit(unit: HeightUnit) {
            viewModelScope.launch {
                userProfileRepo.setHeightUnit(unit)
            }
        }

        fun setDistanceUnit(unit: DistanceUnit) {
            viewModelScope.launch {
                userProfileRepo.setDistanceUnit(unit)
            }
        }

        fun performBackup(outputStream: OutputStream) {
            viewModelScope.launch {
                _backupStatus.value = BackupStatus.Loading
                backupRepository
                    .exportData(outputStream)
                    .onSuccess {
                        _backupStatus.value = BackupStatus.Success("Backup created successfully")
                    }.onFailure {
                        _backupStatus.value = BackupStatus.Error(it.message ?: "Backup failed")
                    }
            }
        }

        fun restoreBackup(
            inputStream: InputStream,
            options: RestoreOptions,
        ) {
            viewModelScope.launch {
                _backupStatus.value = BackupStatus.Loading

                val parseResult = backupRepository.parseBackup(inputStream)

                parseResult
                    .onSuccess { backupData ->
                        backupRepository
                            .restoreData(backupData, options)
                            .onSuccess {
                                _backupStatus.value = BackupStatus.Success("Restore completed successfully")
                            }.onFailure {
                                _backupStatus.value = BackupStatus.Error(it.message ?: "Restore failed")
                            }
                    }.onFailure {
                        _backupStatus.value = BackupStatus.Error(it.message ?: "Failed to parse backup file")
                    }
            }
        }

        fun checkForUpdates() {
            viewModelScope.launch {
                _updateStatus.value = UpdateStatus.Checking
                delay(MOCK_DELAY) // Mock network delay
                _updateStatus.value = UpdateStatus.NoUpdate
            }
        }

        fun clearStatus() {
            _backupStatus.value = BackupStatus.Idle
        }

        fun clearUpdateStatus() {
            _updateStatus.value = UpdateStatus.Idle
        }

        fun copyLogs() {
            try {
                val logFile = File(application.cacheDir, "app_logs.txt")
                if (logFile.exists()) {
                    val content = logFile.readText()
                    val clipboard = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("App Logs", content)
                    clipboard.setPrimaryClip(clip)
                    _backupStatus.value = BackupStatus.Success("Logs copied to clipboard")
                } else {
                    _backupStatus.value = BackupStatus.Error("No logs found")
                }
            } catch (
                @Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception,
            ) {
                Timber.e(e, "Failed to copy logs")
                _backupStatus.value = BackupStatus.Error("Failed to copy logs")
            }
        }

        fun saveLogs(context: Context) {
            try {
                val logFile = File(application.cacheDir, "app_logs.txt")
                if (logFile.exists()) {
                    val uri =
                        FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            logFile,
                        )
                    val intent =
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    context.startActivity(Intent.createChooser(intent, "Save Logs"))
                }
            } catch (
                @Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception,
            ) {
                Timber.e(e, "Failed to save logs")
            }
        }
    }

private const val STOP_TIMEOUT_MILLIS = 5_000L

sealed class BackupStatus {
    object Idle : BackupStatus()

    object Loading : BackupStatus()

    data class Success(
        val message: String,
    ) : BackupStatus()

    data class Error(
        val message: String,
    ) : BackupStatus()
}

sealed class UpdateStatus {
    object Idle : UpdateStatus()

    object Checking : UpdateStatus()

    object NoUpdate : UpdateStatus()

    object UpdateAvailable : UpdateStatus()

    data class Error(
        val message: String,
    ) : UpdateStatus()
}

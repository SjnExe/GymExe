package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.backup.BackupRepository
import com.sjn.gym.core.data.repository.backup.RestoreOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val repo: UserPreferencesRepository,
        private val userProfileRepo: UserProfileRepository,
        private val backupRepository: BackupRepository,
    ) : ViewModel() {
        val themeMode =
            repo.themeMode.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                "SYSTEM",
            )

        val weightUnit =
            userProfileRepo.weightUnit.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                "KG",
            )

        val heightUnit =
            userProfileRepo.heightUnit.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                "CM",
            )

        private val _backupStatus = MutableStateFlow<BackupStatus>(BackupStatus.Idle)
        val backupStatus: StateFlow<BackupStatus> = _backupStatus.asStateFlow()

        private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
        val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

        val appVersion: String = com.sjn.gym.feature.settings.BuildConfig.VERSION_NAME

        fun setTheme(mode: String) {
            viewModelScope.launch {
                repo.setThemeMode(mode)
            }
        }

        fun setWeightUnit(unit: String) {
            viewModelScope.launch {
                userProfileRepo.setWeightUnit(unit)
            }
        }

        fun setHeightUnit(unit: String) {
            viewModelScope.launch {
                userProfileRepo.setHeightUnit(unit)
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
                delay(2000) // Mock network delay
                _updateStatus.value = UpdateStatus.NoUpdate
            }
        }

        fun clearStatus() {
            _backupStatus.value = BackupStatus.Idle
        }

        fun clearUpdateStatus() {
            _updateStatus.value = UpdateStatus.Idle
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

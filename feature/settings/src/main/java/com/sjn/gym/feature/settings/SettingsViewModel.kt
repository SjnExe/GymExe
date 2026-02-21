package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.backup.BackupRepository
import com.sjn.gym.core.data.repository.backup.RestoreOptions
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SettingsViewModel @Inject constructor(
    private val repo: UserPreferencesRepository,
    private val userProfileRepo: UserProfileRepository,
    private val backupRepository: BackupRepository
) : ViewModel() {

    val themeMode = repo.themeMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "SYSTEM"
    )

    val weightUnit = userProfileRepo.weightUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "KG"
    )

    val heightUnit = userProfileRepo.heightUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "CM"
    )

    private val _backupStatus = MutableStateFlow<BackupStatus>(BackupStatus.Idle)
    val backupStatus: StateFlow<BackupStatus> = _backupStatus.asStateFlow()

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
            backupRepository.exportData(outputStream)
                .onSuccess {
                    _backupStatus.value = BackupStatus.Success("Backup created successfully")
                }
                .onFailure {
                    _backupStatus.value = BackupStatus.Error(it.message ?: "Backup failed")
                }
        }
    }

    fun restoreBackup(inputStream: InputStream, options: RestoreOptions) {
        viewModelScope.launch {
            _backupStatus.value = BackupStatus.Loading

            val parseResult = backupRepository.parseBackup(inputStream)

            parseResult.onSuccess { backupData ->
                backupRepository.restoreData(backupData, options)
                    .onSuccess {
                        _backupStatus.value = BackupStatus.Success("Restore completed successfully")
                    }
                    .onFailure {
                         _backupStatus.value = BackupStatus.Error(it.message ?: "Restore failed")
                    }
            }.onFailure {
                _backupStatus.value = BackupStatus.Error(it.message ?: "Failed to parse backup file")
            }
        }
    }

    fun clearStatus() {
        _backupStatus.value = BackupStatus.Idle
    }
}

sealed class BackupStatus {
    object Idle : BackupStatus()
    object Loading : BackupStatus()
    data class Success(val message: String) : BackupStatus()
    data class Error(val message: String) : BackupStatus()
}

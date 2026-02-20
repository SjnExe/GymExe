package com.sjn.gymexe.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import com.sjn.gymexe.domain.manager.DownloadStatus
import com.sjn.gymexe.domain.manager.UpdateManager
import com.sjn.gymexe.domain.manager.UpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SHARING_TIMEOUT = 5000L

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val updateManager: UpdateManager,
) : ViewModel() {
    val themeMode: StateFlow<String> =
        userPreferencesRepository.themeMode
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SHARING_TIMEOUT), "System")

    val useDynamicColors: StateFlow<Boolean> =
        userPreferencesRepository.useDynamicColors
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SHARING_TIMEOUT), true)

    val weightUnit: StateFlow<String> =
        userPreferencesRepository.weightUnit
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SHARING_TIMEOUT), "KG")

    val distanceUnit: StateFlow<String> =
        userPreferencesRepository.distanceUnit
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SHARING_TIMEOUT), "KM")

    // Channel for one-time events (Toast messages, Navigation)
    private val _updateEvents = Channel<UpdateEvent>()
    val updateEvents = _updateEvents.receiveAsFlow()

    private val _downloadStatus = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
    val downloadStatus: StateFlow<DownloadStatus> = _downloadStatus.asStateFlow()

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(mode)
        }
    }

    fun setUseDynamicColors(useDynamic: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setUseDynamicColors(useDynamic)
        }
    }

    fun setWeightUnit(unit: String) {
        viewModelScope.launch {
            userPreferencesRepository.setWeightUnit(unit)
        }
    }

    fun setDistanceUnit(unit: String) {
        viewModelScope.launch {
            userPreferencesRepository.setDistanceUnit(unit)
        }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            _updateEvents.send(UpdateEvent.Checking)
            val result = updateManager.checkForUpdates()
            when (result) {
                is UpdateResult.NoUpdate -> {
                    _updateEvents.send(UpdateEvent.NoUpdate)
                }
                is UpdateResult.UpdateAvailable -> {
                    _updateEvents.send(UpdateEvent.UpdateAvailable(result))
                }
                is UpdateResult.Error -> {
                    _updateEvents.send(UpdateEvent.Error(result.e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun downloadUpdate(url: String, version: String) {
        viewModelScope.launch {
            updateManager.downloadUpdateFlow(url, "GymExe-$version.apk")
                .collect { status ->
                    _downloadStatus.value = status
                }
        }
    }

    fun resetDownloadStatus() {
        _downloadStatus.value = DownloadStatus.Idle
    }

    sealed class UpdateEvent {
        data object Checking : UpdateEvent()
        data object NoUpdate : UpdateEvent()
        data class UpdateAvailable(val update: UpdateResult.UpdateAvailable) : UpdateEvent()
        data class Error(val message: String) : UpdateEvent()
    }
}

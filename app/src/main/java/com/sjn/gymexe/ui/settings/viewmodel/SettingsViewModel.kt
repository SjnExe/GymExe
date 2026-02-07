package com.sjn.gymexe.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import com.sjn.gymexe.domain.manager.UpdateManager
import com.sjn.gymexe.domain.manager.UpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        private val updateManager: UpdateManager,
    ) : ViewModel() {
        val themeMode: StateFlow<String> =
            userPreferencesRepository.themeMode
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

        val useDynamicColors: StateFlow<Boolean> =
            userPreferencesRepository.useDynamicColors
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

        val weightUnit: StateFlow<String> =
            userPreferencesRepository.weightUnit
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "KG")

        val distanceUnit: StateFlow<String> =
            userPreferencesRepository.distanceUnit
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "KM")

        // Channel for one-time events (Toast messages, Navigation)
        private val _updateEvents = Channel<UpdateEvent>()
        val updateEvents = _updateEvents.receiveAsFlow()

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
                        // Optionally auto-download here or prompt user
                        // For now, let's just notify UI
                        updateManager.downloadUpdate(result.url, "GymExe-${result.version}.apk")
                    }
                    is UpdateResult.Error -> {
                        _updateEvents.send(UpdateEvent.Error(result.e.message ?: "Unknown error"))
                    }
                }
            }
        }

        sealed class UpdateEvent {
            data object Checking : UpdateEvent()
            data object NoUpdate : UpdateEvent()
            data class UpdateAvailable(val update: UpdateResult.UpdateAvailable) : UpdateEvent()
            data class Error(val message: String) : UpdateEvent()
        }
    }

package com.sjn.gymexe.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
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
    }

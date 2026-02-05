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

        val useMetricDisplay: StateFlow<Boolean> =
            userPreferencesRepository.useMetricDisplay
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

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

        fun setUseMetricDisplay(useMetric: Boolean) {
            viewModelScope.launch {
                userPreferencesRepository.setUseMetricDisplay(useMetric)
            }
        }
    }

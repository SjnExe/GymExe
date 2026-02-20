package com.sjn.gym.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import com.sjn.gym.core.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: UserPreferencesRepository,
    private val userProfileRepo: UserProfileRepository
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
}

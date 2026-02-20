package com.sjn.gym.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: UserPreferencesRepository
) : ViewModel() {

    val themeMode = repo.themeMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "SYSTEM"
    )

    fun setTheme(mode: String) {
        viewModelScope.launch {
            repo.setThemeMode(mode)
        }
    }
}

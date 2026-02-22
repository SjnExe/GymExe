package com.sjn.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        userPreferencesRepository: UserPreferencesRepository,
        userProfileRepository: UserProfileRepository,
    ) : ViewModel() {
        val uiState: StateFlow<MainActivityUiState> =
            combine(
                userPreferencesRepository.isOnboardingCompleted,
                userProfileRepository.themeConfig,
                userProfileRepository.themeStyle,
                userProfileRepository.customThemeColor,
            ) { isOnboardingCompleted, themeConfig, themeStyle, customThemeColor ->
                MainActivityUiState.Success(
                    UserData(
                        isOnboardingCompleted = isOnboardingCompleted,
                        themeConfig = themeConfig,
                        themeStyle = themeStyle,
                        customThemeColor = customThemeColor,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = MainActivityUiState.Loading,
            )
    }

private const val STOP_TIMEOUT_MILLIS = 5_000L

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(
        val userData: UserData,
    ) : MainActivityUiState
}

data class UserData(
    val isOnboardingCompleted: Boolean,
    val themeConfig: ThemeConfig,
    val themeStyle: ThemeStyle,
    val customThemeColor: Int?,
)

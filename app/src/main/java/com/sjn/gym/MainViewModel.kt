package com.sjn.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserPreferencesRepository
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
        private val repo: UserPreferencesRepository,
    ) : ViewModel() {
        val uiState: StateFlow<MainActivityUiState> =
            combine(
                repo.themeMode,
                repo.useDynamicColor,
                repo.isOnboardingCompleted,
            ) { themeMode, useDynamicColor, isOnboardingCompleted ->
                MainActivityUiState.Success(
                    UserData(themeMode, useDynamicColor, isOnboardingCompleted),
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
    val themeMode: String,
    val useDynamicColor: Boolean,
    val isOnboardingCompleted: Boolean,
)

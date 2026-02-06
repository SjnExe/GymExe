package com.sjn.gymexe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import com.sjn.gymexe.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val exerciseRepository: ExerciseRepository,
        private val userPreferencesRepository: UserPreferencesRepository,
    ) : ViewModel() {
        val themeMode: StateFlow<String> =
            userPreferencesRepository.themeMode
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

        val useDynamicColors: StateFlow<Boolean> =
            userPreferencesRepository.useDynamicColors
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

        init {
            viewModelScope.launch {
                exerciseRepository.syncExercisesFromAssets()
            }
        }
    }

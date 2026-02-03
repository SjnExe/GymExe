package com.gym.exe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gym.exe.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val exerciseRepository: ExerciseRepository,
    ) : ViewModel() {
        init {
            viewModelScope.launch {
                exerciseRepository.syncExercisesFromAssets()
            }
        }
    }

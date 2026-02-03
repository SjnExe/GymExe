package com.gym.exe.ui.screens.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gym.exe.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel
    @Inject
    constructor(
        private val repository: ExerciseRepository,
    ) : ViewModel() {
        val exercises =
            repository.getAllExercises()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

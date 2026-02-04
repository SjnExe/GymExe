package com.sjn.gymexe.ui.screens.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.domain.repository.ExerciseRepository
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
            repository
                .getAllExercises()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

        companion object {
            private const val STOP_TIMEOUT_MILLIS = 5000L
        }
    }

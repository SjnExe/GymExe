package com.sjn.gymexe.ui.screens.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.local.entity.ExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel
    @Inject
    constructor() : ViewModel() {
        // Placeholder until getAllExercises is implemented in Repository
        val exercises =
             flowOf(emptyList<ExerciseEntity>())
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

        companion object {
            private const val STOP_TIMEOUT_MILLIS = 5000L
        }
    }

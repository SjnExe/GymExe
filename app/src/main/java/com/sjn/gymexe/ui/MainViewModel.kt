package com.sjn.gymexe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.domain.repository.ExerciseRepository
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

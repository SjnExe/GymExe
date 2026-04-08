package com.sjn.gym.feature.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.ExerciseRepository
import com.sjn.gym.core.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ExerciseDetailsViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val exerciseId: String = savedStateHandle.get<String>("exerciseId") ?: ""

    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise

    init {
        viewModelScope.launch {
            if (exerciseId.isNotBlank()) {
                _exercise.value = exerciseRepository.getExercise(exerciseId)
            }
        }
    }
}

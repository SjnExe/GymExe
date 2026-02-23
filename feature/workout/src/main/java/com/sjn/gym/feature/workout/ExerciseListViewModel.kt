package com.sjn.gym.feature.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.ExerciseRepository
import com.sjn.gym.core.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel
    @Inject
    constructor(
        private val exerciseRepository: ExerciseRepository,
    ) : ViewModel() {

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery

        val exercises: StateFlow<List<Exercise>> =
            combine(
                exerciseRepository.getAllExercises(),
                _searchQuery,
            ) { exercises, query ->
                if (query.isBlank()) {
                    exercises
                } else {
                    exercises.filter {
                        it.name.contains(query, ignoreCase = true) ||
                            it.targetMuscle.contains(query, ignoreCase = true) ||
                            it.bodyPart.contains(query, ignoreCase = true)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

        fun onSearchQueryChange(newQuery: String) {
            _searchQuery.value = newQuery
        }
    }

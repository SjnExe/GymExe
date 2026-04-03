package com.sjn.gym.feature.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.ExerciseRepository
import com.sjn.gym.core.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ExerciseListViewModel
@Inject
constructor(private val exerciseRepository: ExerciseRepository) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    private val _selectedEquipmentFilter = MutableStateFlow<String?>(null)
    val selectedEquipmentFilter: StateFlow<String?> = _selectedEquipmentFilter
    private val _selectedMuscleFilter = MutableStateFlow<String?>(null)
    val selectedMuscleFilter: StateFlow<String?> = _selectedMuscleFilter

    val exercises: StateFlow<List<Exercise>> =
        combine(
                exerciseRepository.getAllExercises(),
                _searchQuery,
                _selectedEquipmentFilter,
                _selectedMuscleFilter,
            ) { exercises, query, equipmentFilter, muscleFilter ->
                var filtered = exercises
                if (query.isNotBlank())
                    filtered =
                        filtered.filter {
                            it.name.contains(query, true) ||
                                it.targetMuscle.contains(query, true) ||
                                it.bodyPart.contains(query, true)
                        }
                if (equipmentFilter != null)
                    filtered = filtered.filter { it.equipment.equals(equipmentFilter, true) }
                if (muscleFilter != null)
                    filtered =
                        filtered.filter {
                            it.targetMuscle.equals(muscleFilter, true) ||
                                it.bodyPart.equals(muscleFilter, true)
                        }
                filtered
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableEquipments: StateFlow<List<String>> =
        exerciseRepository
            .getAllExercises()
            .combine(exerciseRepository.getAllExercises()) { list, _ ->
                list.map { it.equipment }.filter { it.isNotBlank() }.distinct().sorted()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableMuscles: StateFlow<List<String>> =
        exerciseRepository
            .getAllExercises()
            .combine(exerciseRepository.getAllExercises()) { list, _ ->
                list.map { it.targetMuscle }.filter { it.isNotBlank() }.distinct().sorted()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onEquipmentFilterChange(equipment: String?) {
        _selectedEquipmentFilter.value =
            if (_selectedEquipmentFilter.value == equipment) null else equipment
    }

    fun onMuscleFilterChange(muscle: String?) {
        _selectedMuscleFilter.value = if (_selectedMuscleFilter.value == muscle) null else muscle
    }
}

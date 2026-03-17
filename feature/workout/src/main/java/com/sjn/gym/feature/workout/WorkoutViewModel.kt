package com.sjn.gym.feature.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.ExerciseRepository
import com.sjn.gym.core.model.Exercise
import com.sjn.gym.feature.workout.logic.PlateCount
import com.sjn.gym.feature.workout.logic.WeightInputParser
import com.sjn.gym.feature.workout.logic.WeightParsingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutViewModel
@Inject
constructor(
    private val exerciseRepository: ExerciseRepository,
    private val weightInputParser: WeightInputParser,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Argument from Navigation
    private val exerciseId: String? = savedStateHandle.get<String>("exerciseId")

    var exercise by mutableStateOf<Exercise?>(null)
        private set

    var input by mutableStateOf("")
        private set

    var plateResult by mutableStateOf<List<PlateCount>>(emptyList())
        private set

    var validationError by mutableStateOf<String?>(null)
        private set

    // Derived property for UI to know if we are in "Strict Mode" (Dumbbell/Machine)
    // defaulting to Stackable (false) if exercise is null (Generic Mode)
    val isStrictEquipment: Boolean
        get() = false

    init {
        if (exerciseId != null) {
            viewModelScope.launch {
                exercise = exerciseRepository.getExercise(exerciseId)
                // Re-validate input if exercise loads after input (unlikely but possible)
                onInputChange(input)
            }
        }
    }

    fun onInputChange(newInput: String) {
        input = newInput

        // Default to "Barbell" if no exercise is selected (Generic Calculator Mode)
        val equipmentType = exercise?.equipment ?: "Barbell"

        when (val result = weightInputParser.parse(newInput, equipmentType)) {
            is WeightParsingResult.Success -> {
                validationError = null
                // Convert List<Double> to List<PlateCount>
                plateResult =
                    result.weights
                        .groupBy { it }
                        .map { (weight, list) -> PlateCount(weight, list.size) }
                        .sortedBy { it.weight }
            }

            is WeightParsingResult.Failure -> {
                validationError = result.message
                plateResult = emptyList()
            }
        }
    }
}

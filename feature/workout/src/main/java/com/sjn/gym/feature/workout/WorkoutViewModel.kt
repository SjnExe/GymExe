package com.sjn.gym.feature.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sjn.gym.feature.workout.logic.PlateCalculator
import com.sjn.gym.feature.workout.logic.PlateCount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val plateCalculator: PlateCalculator
) : ViewModel() {

    var input by mutableStateOf("")
        private set

    var plateResult by mutableStateOf<List<PlateCount>>(emptyList())
        private set

    fun onInputChange(newInput: String) {
        input = newInput
        plateResult = plateCalculator.parse(newInput)
    }
}

package com.gym.exe.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gym.exe.data.local.entity.SetEntity
import com.gym.exe.domain.manager.TimerManager
import com.gym.exe.domain.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val timerManager: TimerManager
) : ViewModel() {

    // Ideally we'd get the active program and its current workout.
    // For simplicity in this demo, we'll just fetch the first available workout or empty.
    val activeProgram = programRepository.getActiveProgram()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // In a real app, we'd have a SessionManager to hold current sets.
    // Here we'll just keep a local list of sets being logged.
    private val _currentSets = MutableStateFlow<List<SetEntity>>(emptyList())
    val currentSets = _currentSets.asStateFlow()

    val restTimerState = timerManager.restTimerState

    fun logSet(exerciseId: String, weight: Float, reps: Int, rpe: Float?) {
        val newSet = SetEntity(
            sessionId = 0, // 0 for now as we don't have a real session yet
            exerciseId = exerciseId,
            weight = weight,
            reps = reps,
            rpe = rpe,
            timestamp = System.currentTimeMillis()
        )
        _currentSets.value += newSet

        // Auto-start rest timer (e.g., 90s)
        timerManager.startRestTimer(90_000)
    }

    fun finishWorkout() {
        // Save session logic would go here
    }
}

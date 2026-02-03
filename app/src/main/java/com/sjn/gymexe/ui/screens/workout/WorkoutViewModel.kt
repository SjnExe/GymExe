package com.sjn.gymexe.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.domain.manager.TimerManager
import com.sjn.gymexe.domain.repository.ProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel
    @Inject
    constructor(
        private val programRepository: ProgramRepository,
        private val timerManager: TimerManager,
        private val sessionDao: com.sjn.gymexe.data.local.dao.SessionDao,
        private val setDao: com.sjn.gymexe.data.local.dao.SetDao,
    ) : ViewModel() {
        // Ideally we'd get the active program and its current workout.
        // For simplicity in this demo, we'll just fetch the first available workout or empty.
        val activeProgram =
            programRepository.getActiveProgram()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), null)

        // In a real app, we'd have a SessionManager to hold current sets.
        // Here we'll just keep a local list of sets being logged.
        private val _currentSets = MutableStateFlow<List<SetEntity>>(emptyList())
        val currentSets = _currentSets.asStateFlow()

        val restTimerState = timerManager.restTimerState

        fun logSet(
            exerciseId: String,
            weight: Float,
            reps: Int,
            rpe: Float?,
        ) {
            val newSet =
                SetEntity(
                    // 0 for now as we don't have a real session yet
                    sessionId = 0,
                    exerciseId = exerciseId,
                    weight = weight,
                    reps = reps,
                    rpe = rpe,
                    timestamp = System.currentTimeMillis(),
                )
            _currentSets.value += newSet

            // Auto-start rest timer (e.g., 90s)
            timerManager.startRestTimer(DEFAULT_REST_TIMER_MS)
        }

        fun finishWorkout() {
            viewModelScope.launch {
                val sets = _currentSets.value
                if (sets.isNotEmpty()) {
                    val program = activeProgram.value
                    val sessionName = program?.name ?: "Quick Workout"

                    val start = sets.firstOrNull()?.timestamp ?: System.currentTimeMillis()
                    val end = System.currentTimeMillis()

                    val session =
                        com.sjn.gymexe.data.local.entity.SessionEntity(
                            workoutName = sessionName,
                            startTime = start,
                            endTime = end,
                        )

                    val sessionId = sessionDao.insertSession(session)

                    val setsWithSessionId = sets.map { it.copy(sessionId = sessionId) }
                    setDao.insertSets(setsWithSessionId)

                    // Clear state
                    _currentSets.value = emptyList()
                }
            }
        }

        companion object {
            private const val STOP_TIMEOUT_MILLIS = 5000L
            private const val DEFAULT_REST_TIMER_MS = 90_000L
        }
    }

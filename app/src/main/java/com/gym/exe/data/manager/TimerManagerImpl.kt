package com.gym.exe.data.manager

import com.gym.exe.domain.manager.TimerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerManagerImpl : TimerManager {
    private val _restTimerState = MutableStateFlow(0L)
    override val restTimerState = _restTimerState.asStateFlow()

    private var timerJob: Job? = null

    override fun startRestTimer(durationMillis: Long) {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            val endTime = System.currentTimeMillis() + durationMillis
            while (isActive) {
                val remaining = endTime - System.currentTimeMillis()
                if (remaining <= 0) {
                    _restTimerState.value = 0
                    break
                }
                _restTimerState.value = remaining
                delay(100)
            }
        }
    }

    override fun cancelRestTimer() {
        timerJob?.cancel()
        _restTimerState.value = 0
    }
}

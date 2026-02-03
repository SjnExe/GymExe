package com.sjn.gymexe.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface TimerManager {
    val restTimerState: StateFlow<Long>

    fun startRestTimer(durationMillis: Long)

    fun cancelRestTimer()
}

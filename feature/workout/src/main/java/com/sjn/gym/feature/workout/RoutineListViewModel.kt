package com.sjn.gym.feature.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.RoutineRepository
import com.sjn.gym.core.model.Routine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface RoutineListUiState {
    data object Loading : RoutineListUiState

    data class Success(val routines: ImmutableList<Routine>) : RoutineListUiState

    data class Error(val message: String) : RoutineListUiState
}

@HiltViewModel
class RoutineListViewModel @Inject constructor(private val routineRepository: RoutineRepository) :
    ViewModel() {

    val uiState: StateFlow<RoutineListUiState> =
        routineRepository
            .getAllRoutines()
            .map { routines -> RoutineListUiState.Success(routines.toImmutableList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RoutineListUiState.Loading,
            )
}

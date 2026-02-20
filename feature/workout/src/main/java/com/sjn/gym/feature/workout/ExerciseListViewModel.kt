package com.sjn.gym.feature.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.database.dao.ExerciseDao
import com.sjn.gym.core.data.database.entity.ExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    exerciseDao: ExerciseDao
) : ViewModel() {
    val exercises: StateFlow<List<ExerciseEntity>> = exerciseDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

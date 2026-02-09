package com.sjn.gymexe.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gymexe.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val availablePlates = userPreferencesRepository.availablePlates
    val availableDumbbells = userPreferencesRepository.availableDumbbells

    fun addPlate(weight: Float) {
        viewModelScope.launch {
            val current = availablePlates.first().toMutableList()
            if (!current.contains(weight)) {
                current.add(weight)
                userPreferencesRepository.setAvailablePlates(current.sorted())
            }
        }
    }

    fun removePlate(weight: Float) {
        viewModelScope.launch {
            val current = availablePlates.first().toMutableList()
            current.remove(weight)
            userPreferencesRepository.setAvailablePlates(current)
        }
    }

    fun addDumbbell(weight: Float) {
        viewModelScope.launch {
            val current = availableDumbbells.first().toMutableList()
            if (!current.contains(weight)) {
                current.add(weight)
                userPreferencesRepository.setAvailableDumbbells(current.sorted())
            }
        }
    }

    fun removeDumbbell(weight: Float) {
        viewModelScope.launch {
            val current = availableDumbbells.first().toMutableList()
            current.remove(weight)
            userPreferencesRepository.setAvailableDumbbells(current)
        }
    }
}

package com.sjn.gym.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _currentStep = MutableStateFlow(0)
    val currentStep = _currentStep.asStateFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesRepository.setOnboardingCompleted(true)
        }
    }

    fun saveProfileData(
        gender: String?,
        weight: Double?,
        weightUnit: String,
        height: Double?,
        heightUnit: String
    ) {
        viewModelScope.launch {
            if (gender != null) userProfileRepository.setGender(gender)
            if (weight != null) userProfileRepository.setWeight(weight, weightUnit)
            if (height != null) userProfileRepository.setHeight(height, heightUnit)
        }
    }

    fun saveExperienceLevel(level: String) {
        viewModelScope.launch {
            userProfileRepository.setExperienceLevel(level)
        }
    }

    fun saveEquipment(equipment: Set<String>) {
        viewModelScope.launch {
            userProfileRepository.setEquipmentList(equipment)
        }
    }

    fun nextStep() {
        _currentStep.value += 1
    }

    fun prevStep() {
        if (_currentStep.value > 0) _currentStep.value -= 1
    }
}

package com.sjn.gym.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.WeightUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val age: Int = 0,
    val gender: String? = null,
    val weightValue: Double? = null,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val heightValue: Double? = null,
    val heightUnit: HeightUnit = HeightUnit.CM,
    val experienceLevel: String? = null,
    val equipmentList: Set<String> = emptySet(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private val userProfileRepository: UserProfileRepository) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> =
        combine(
                userProfileRepository.userProfile,
                userProfileRepository.weightUnit,
                userProfileRepository.heightUnit,
                userProfileRepository.experienceLevel,
                userProfileRepository.equipmentList,
            ) { profile, wUnit, hUnit, exp, equip ->
                ProfileUiState(
                    name = profile?.name ?: "",
                    age = profile?.age ?: 0,
                    gender = profile?.gender?.name,
                    weightValue = profile?.weight,
                    weightUnit = wUnit,
                    heightValue = profile?.height,
                    heightUnit = hUnit,
                    experienceLevel = exp,
                    equipmentList = equip,
                    isLoading = false,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProfileUiState(),
            )

    fun setName(name: String) {
        viewModelScope.launch { userProfileRepository.setName(name) }
    }

    fun setAge(age: Int) {
        viewModelScope.launch { userProfileRepository.setAge(age) }
    }

    fun setGender(gender: String) {
        viewModelScope.launch { userProfileRepository.setGender(gender) }
    }

    fun setWeight(value: Double, unit: WeightUnit) {
        viewModelScope.launch { userProfileRepository.setWeight(value, unit.name) }
    }

    fun setHeight(value: Double, unit: HeightUnit) {
        viewModelScope.launch { userProfileRepository.setHeight(value, unit.name) }
    }

    fun setExperienceLevel(level: String) {
        viewModelScope.launch { userProfileRepository.setExperienceLevel(level) }
    }

    fun setEquipmentList(equipment: Set<String>) {
        viewModelScope.launch { userProfileRepository.setEquipmentList(equipment) }
    }
}

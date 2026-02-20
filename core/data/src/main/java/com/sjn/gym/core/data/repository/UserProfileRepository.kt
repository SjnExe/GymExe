package com.sjn.gym.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.userProfileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

@Singleton
class UserProfileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.userProfileDataStore

    private object ProfileKeys {
        val GENDER = stringPreferencesKey("gender")
        val WEIGHT_VALUE = doublePreferencesKey("weight_value")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit") // KG, LBS
        val HEIGHT_VALUE = doublePreferencesKey("height_value")
        val HEIGHT_UNIT = stringPreferencesKey("height_unit") // CM, FT
        val EXPERIENCE_LEVEL = stringPreferencesKey("experience_level")
        val EQUIPMENT_LIST = stringSetPreferencesKey("equipment_list")
    }

    val gender: Flow<String?> = dataStore.data.map { it[ProfileKeys.GENDER] }
    val weightValue: Flow<Double?> = dataStore.data.map { it[ProfileKeys.WEIGHT_VALUE] }
    val weightUnit: Flow<String> = dataStore.data.map { it[ProfileKeys.WEIGHT_UNIT] ?: "KG" }
    val heightValue: Flow<Double?> = dataStore.data.map { it[ProfileKeys.HEIGHT_VALUE] }
    val heightUnit: Flow<String> = dataStore.data.map { it[ProfileKeys.HEIGHT_UNIT] ?: "CM" }
    val experienceLevel: Flow<String?> = dataStore.data.map { it[ProfileKeys.EXPERIENCE_LEVEL] }
    val equipmentList: Flow<Set<String>> = dataStore.data.map { it[ProfileKeys.EQUIPMENT_LIST] ?: emptySet() }

    suspend fun setGender(gender: String) {
        dataStore.edit { it[ProfileKeys.GENDER] = gender }
    }

    suspend fun setWeight(value: Double, unit: String) {
        dataStore.edit {
            it[ProfileKeys.WEIGHT_VALUE] = value
            it[ProfileKeys.WEIGHT_UNIT] = unit
        }
    }

    suspend fun setHeight(value: Double, unit: String) {
        dataStore.edit {
            it[ProfileKeys.HEIGHT_VALUE] = value
            it[ProfileKeys.HEIGHT_UNIT] = unit
        }
    }

    suspend fun setExperienceLevel(level: String) {
        dataStore.edit { it[ProfileKeys.EXPERIENCE_LEVEL] = level }
    }

    suspend fun setEquipmentList(equipment: Set<String>) {
        dataStore.edit { it[ProfileKeys.EQUIPMENT_LIST] = equipment }
    }
}

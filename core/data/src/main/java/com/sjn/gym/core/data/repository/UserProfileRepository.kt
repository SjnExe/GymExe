package com.sjn.gym.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.Gender
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.UserProfile
import com.sjn.gym.core.model.WeightUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.userProfileDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

@Singleton
class UserProfileRepository
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        private val dataStore = context.userProfileDataStore

        private object ProfileKeys {
            val NAME = stringPreferencesKey("name")
            val AGE = intPreferencesKey("age")
            val GENDER = stringPreferencesKey("gender")
            val WEIGHT_VALUE = doublePreferencesKey("weight_value")
            val WEIGHT_UNIT = stringPreferencesKey("weight_unit") // KG, LBS
            val HEIGHT_VALUE = doublePreferencesKey("height_value")
            val HEIGHT_UNIT = stringPreferencesKey("height_unit") // CM, FT
            val EXPERIENCE_LEVEL = stringPreferencesKey("experience_level")
            val EQUIPMENT_LIST = stringSetPreferencesKey("equipment_list")
            val THEME_CONFIG = stringPreferencesKey("theme_config")
            val THEME_STYLE = stringPreferencesKey("theme_style")
            val CUSTOM_THEME_COLOR = intPreferencesKey("custom_theme_color")
            val DISTANCE_UNIT = stringPreferencesKey("distance_unit")
        }

        val gender: Flow<String?> = dataStore.data.map { it[ProfileKeys.GENDER] }
        val weightValue: Flow<Double?> = dataStore.data.map { it[ProfileKeys.WEIGHT_VALUE] }

        val weightUnit: Flow<WeightUnit> =
            dataStore.data.map { prefs ->
                prefs[ProfileKeys.WEIGHT_UNIT]?.let {
                    try {
                        WeightUnit.valueOf(it)
                    } catch (e: Exception) {
                        WeightUnit.KG
                    }
                } ?: WeightUnit.KG
            }

        val heightValue: Flow<Double?> = dataStore.data.map { it[ProfileKeys.HEIGHT_VALUE] }

        val heightUnit: Flow<HeightUnit> =
            dataStore.data.map { prefs ->
                prefs[ProfileKeys.HEIGHT_UNIT]?.let {
                    try {
                        HeightUnit.valueOf(it)
                    } catch (e: Exception) {
                        HeightUnit.CM
                    }
                } ?: HeightUnit.CM
            }

        val experienceLevel: Flow<String?> = dataStore.data.map { it[ProfileKeys.EXPERIENCE_LEVEL] }
        val equipmentList: Flow<Set<String>> = dataStore.data.map { it[ProfileKeys.EQUIPMENT_LIST] ?: emptySet() }

        val themeConfig: Flow<ThemeConfig> =
            dataStore.data.map { prefs ->
                prefs[ProfileKeys.THEME_CONFIG]?.let {
                    try {
                        ThemeConfig.valueOf(it)
                    } catch (e: Exception) {
                        ThemeConfig.SYSTEM
                    }
                } ?: ThemeConfig.SYSTEM
            }

        val themeStyle: Flow<ThemeStyle> =
            dataStore.data.map { prefs ->
                prefs[ProfileKeys.THEME_STYLE]?.let {
                    try {
                        ThemeStyle.valueOf(it)
                    } catch (e: Exception) {
                        ThemeStyle.DYNAMIC
                    }
                } ?: ThemeStyle.DYNAMIC
            }

        val customThemeColor: Flow<Int?> = dataStore.data.map { it[ProfileKeys.CUSTOM_THEME_COLOR] }

        val distanceUnit: Flow<DistanceUnit> =
            dataStore.data.map { prefs ->
                prefs[ProfileKeys.DISTANCE_UNIT]?.let {
                    try {
                        DistanceUnit.valueOf(it)
                    } catch (e: Exception) {
                        DistanceUnit.KM
                    }
                } ?: DistanceUnit.KM
            }

        val userProfile: Flow<UserProfile?> =
            dataStore.data.map { preferences ->
                val name = preferences[ProfileKeys.NAME] ?: "User"
                val age = preferences[ProfileKeys.AGE] ?: 0
                val weight = preferences[ProfileKeys.WEIGHT_VALUE] ?: 0.0
                val height = preferences[ProfileKeys.HEIGHT_VALUE] ?: 0.0
                val genderStr = preferences[ProfileKeys.GENDER]
                val gender =
                    genderStr?.let {
                        try {
                            Gender.valueOf(it)
                        } catch (
                            @Suppress("SwallowedException") e: IllegalArgumentException,
                        ) {
                            Gender.MALE
                        }
                    } ?: Gender.MALE

                UserProfile(
                    id = "local",
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    gender = gender,
                )
            }

        suspend fun setGender(gender: String) {
            dataStore.edit { it[ProfileKeys.GENDER] = gender }
        }

        suspend fun setWeight(
            value: Double,
            unit: String,
        ) {
            dataStore.edit {
                it[ProfileKeys.WEIGHT_VALUE] = value
                it[ProfileKeys.WEIGHT_UNIT] = unit
            }
        }

        suspend fun setHeight(
            value: Double,
            unit: String,
        ) {
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

        suspend fun setWeightUnit(unit: WeightUnit) {
            dataStore.edit { it[ProfileKeys.WEIGHT_UNIT] = unit.name }
        }

        suspend fun setHeightUnit(unit: HeightUnit) {
            dataStore.edit { it[ProfileKeys.HEIGHT_UNIT] = unit.name }
        }

        suspend fun setThemeConfig(config: ThemeConfig) {
            dataStore.edit { it[ProfileKeys.THEME_CONFIG] = config.name }
        }

        suspend fun setThemeStyle(style: ThemeStyle) {
            dataStore.edit { it[ProfileKeys.THEME_STYLE] = style.name }
        }

        suspend fun setCustomThemeColor(color: Int) {
            dataStore.edit { it[ProfileKeys.CUSTOM_THEME_COLOR] = color }
        }

        suspend fun setDistanceUnit(unit: DistanceUnit) {
            dataStore.edit { it[ProfileKeys.DISTANCE_UNIT] = unit.name }
        }

        suspend fun saveProfile(profile: UserProfile) {
            dataStore.edit { preferences ->
                preferences[ProfileKeys.NAME] = profile.name
                preferences[ProfileKeys.AGE] = profile.age
                preferences[ProfileKeys.WEIGHT_VALUE] = profile.weight
                preferences[ProfileKeys.HEIGHT_VALUE] = profile.height
                preferences[ProfileKeys.GENDER] = profile.gender.name
            }
        }
    }

package com.sjn.gymexe.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode") // System, Dark, Light
        val USE_DYNAMIC_COLORS = booleanPreferencesKey("use_dynamic_colors")
        // Replaced generic boolean with specific string keys
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit") // "KG" or "LBS"
        val DISTANCE_UNIT = stringPreferencesKey("distance_unit") // "KM" or "MILES"
        // Equipment weights (JSON strings)
        val AVAILABLE_PLATES = stringPreferencesKey("available_plates")
        val AVAILABLE_DUMBBELLS = stringPreferencesKey("available_dumbbells")
    }

    // Defaults
    private val defaultPlates = listOf(0.5f, 1.25f, 2.5f, 5f, 10f, 15f, 20f, 25f)
    private val defaultDumbbells = listOf(1f, 2f, 2.5f, 5f, 7.5f, 10f, 12.5f, 15f, 17.5f, 20f, 25f, 30f)

    val themeMode: Flow<String> =
        context.dataStore.data
            .map { preferences -> preferences[Keys.THEME_MODE] ?: "System" }

    val useDynamicColors: Flow<Boolean> =
        context.dataStore.data
            .map { preferences -> preferences[Keys.USE_DYNAMIC_COLORS] ?: true }

    val weightUnit: Flow<String> =
        context.dataStore.data
            .map { preferences -> preferences[Keys.WEIGHT_UNIT] ?: "KG" }

    val distanceUnit: Flow<String> =
        context.dataStore.data
            .map { preferences -> preferences[Keys.DISTANCE_UNIT] ?: "KM" }

    val availablePlates: Flow<List<Float>> =
        context.dataStore.data
            .map { preferences ->
                val json = preferences[Keys.AVAILABLE_PLATES]
                if (json != null) {
                    try {
                        Json.decodeFromString<List<Float>>(json)
                    } catch (e: Exception) {
                        defaultPlates
                    }
                } else {
                    defaultPlates
                }
            }

    val availableDumbbells: Flow<List<Float>> =
        context.dataStore.data
            .map { preferences ->
                val json = preferences[Keys.AVAILABLE_DUMBBELLS]
                if (json != null) {
                    try {
                        Json.decodeFromString<List<Float>>(json)
                    } catch (e: Exception) {
                        defaultDumbbells
                    }
                } else {
                    defaultDumbbells
                }
            }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = mode
        }
    }

    suspend fun setUseDynamicColors(useDynamic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USE_DYNAMIC_COLORS] = useDynamic
        }
    }

    suspend fun setWeightUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.WEIGHT_UNIT] = unit
        }
    }

    suspend fun setDistanceUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DISTANCE_UNIT] = unit
        }
    }

    suspend fun setAvailablePlates(plates: List<Float>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.AVAILABLE_PLATES] = Json.encodeToString(plates)
        }
    }

    suspend fun setAvailableDumbbells(dumbbells: List<Float>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.AVAILABLE_DUMBBELLS] = Json.encodeToString(dumbbells)
        }
    }
}

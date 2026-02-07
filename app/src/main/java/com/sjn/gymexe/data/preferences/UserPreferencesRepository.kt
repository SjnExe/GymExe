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
        }

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
    }

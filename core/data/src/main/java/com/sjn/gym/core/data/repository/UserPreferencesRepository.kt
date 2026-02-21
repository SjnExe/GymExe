package com.sjn.gym.core.data.repository

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
import com.sjn.gym.core.model.UserPreferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val THEME_MODE = stringPreferencesKey("theme_mode") // SYSTEM, LIGHT, DARK
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val UNIT_SYSTEM = stringPreferencesKey("unit_system") // METRIC, IMPERIAL
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
        }

    val themeMode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_MODE] ?: "SYSTEM"
        }

    val useDynamicColor: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true
        }

    val unitSystem: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.UNIT_SYSTEM] ?: "METRIC"
        }

    val userData: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                isOnboardingCompleted = preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false,
                themeMode = preferences[PreferencesKeys.THEME_MODE] ?: "SYSTEM",
                useDynamicColor = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true,
            )
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun setUseDynamicColor(useDynamic: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = useDynamic
        }
    }

    suspend fun setUnitSystem(system: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.UNIT_SYSTEM] = system
        }
    }
}

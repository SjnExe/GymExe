package com.sjn.gym.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    Column {
        Text("Current Theme: $themeMode")
        Button(onClick = { viewModel.setTheme("DARK") }) {
            Text("Set Dark Theme")
        }
        Button(onClick = { viewModel.setTheme("LIGHT") }) {
            Text("Set Light Theme")
        }
        Button(onClick = { viewModel.setTheme("SYSTEM") }) {
            Text("Set System Theme")
        }

        Text("Unit Settings")
        // Placeholder for unit selection

        Text("Backup & Restore")
        Button(onClick = { /* TODO */ }) { Text("Backup Now") }
        Button(onClick = { /* TODO */ }) { Text("Restore") }

        Text("Updates")
        Button(onClick = { /* TODO */ }) { Text("Check for Updates") }

        Text("Developer Options")
        Button(onClick = { /* TODO */ }) { Text("Save Logs") }
    }
}

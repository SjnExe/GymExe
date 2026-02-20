package com.sjn.gym.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.ui.components.SegmentedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle()
    val heightUnit by viewModel.heightUnit.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Theme Section
            SettingsSection("Appearance") {
                Text("Theme", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("LIGHT", "DARK", "SYSTEM"),
                    selectedOption = themeMode,
                    onOptionSelected = { viewModel.setTheme(it) }
                )
            }

            // Units Section
            SettingsSection("Units") {
                Text("Weight Unit", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("KG", "LBS"),
                    selectedOption = weightUnit,
                    onOptionSelected = { viewModel.setWeightUnit(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Height Unit", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("CM", "FT"),
                    selectedOption = heightUnit,
                    onOptionSelected = { viewModel.setHeightUnit(it) }
                )
            }

            // Data Section
            SettingsSection("Data") {
                Button(
                    onClick = { /* TODO: Backup */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Backup Data")
                }
                Button(
                    onClick = { /* TODO: Restore */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restore Data")
                }
            }

            // About Section
            SettingsSection("About") {
                Text("GymExe v1.0.0", style = MaterialTheme.typography.bodySmall)
                Button(
                    onClick = { /* Check Updates */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Check for Updates")
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

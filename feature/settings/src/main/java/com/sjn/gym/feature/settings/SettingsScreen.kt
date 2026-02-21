package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.ui.components.SegmentedButton
import com.sjn.gym.feature.settings.components.RestoreOptionsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle()
    val heightUnit by viewModel.heightUnit.collectAsStateWithLifecycle()
    val backupStatus by viewModel.backupStatus.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // State for restore flow
    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for creating a backup file
    val createDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
        ) { uri ->
            uri?.let {
                try {
                    context.contentResolver.openOutputStream(it)?.let { outputStream ->
                        viewModel.performBackup(outputStream)
                    }
                } catch (@Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception) {
                    // Handle error
                }
            }
        }

    // Launcher for opening a backup file
    val openDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
        ) { uri ->
            uri?.let {
                restoreUri = it
                showRestoreDialog = true
            }
        }

    LaunchedEffect(backupStatus) {
        when (val status = backupStatus) {
            is BackupStatus.Success -> {
                snackbarHostState.showSnackbar(status.message)
                viewModel.clearStatus()
            }

            is BackupStatus.Error -> {
                snackbarHostState.showSnackbar(status.message)
                viewModel.clearStatus()
            }

            else -> {}
        }
    }

    if (showRestoreDialog && restoreUri != null) {
        RestoreOptionsDialog(
            onDismissRequest = {
                showRestoreDialog = false
                restoreUri = null
            },
            onConfirm = { options ->
                try {
                    context.contentResolver.openInputStream(restoreUri!!)?.let { inputStream ->
                        viewModel.restoreBackup(inputStream, options)
                    }
                } catch (@Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception) {
                    // Handle error
                }
                showRestoreDialog = false
                restoreUri = null
            },
        )
    }

    // Loading overlay
    if (backupStatus is BackupStatus.Loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Theme Section
            SettingsSection("Appearance") {
                Text("Theme", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("LIGHT", "DARK", "SYSTEM"),
                    selectedOption = themeMode,
                    onOptionSelected = { viewModel.setTheme(it) },
                )
            }

            // Units Section
            SettingsSection("Units") {
                Text("Weight Unit", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("KG", "LBS"),
                    selectedOption = weightUnit,
                    onOptionSelected = { viewModel.setWeightUnit(it) },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Height Unit", style = MaterialTheme.typography.bodyMedium)
                SegmentedButton(
                    options = listOf("CM", "FT"),
                    selectedOption = heightUnit,
                    onOptionSelected = { viewModel.setHeightUnit(it) },
                )
            }

            // Data Section
            SettingsSection("Data") {
                Button(
                    onClick = { createDocumentLauncher.launch("backup.gym") },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Backup Data")
                }
                Button(
                    onClick = { openDocumentLauncher.launch(arrayOf("*/*")) }, // Ideally application/octet-stream or custom mime
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Restore Data")
                }
            }

            // About Section
            SettingsSection("About") {
                Text("GymExe v1.0.0", style = MaterialTheme.typography.bodySmall)
                Button(
                    onClick = { /* Check Updates */ },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Check for Updates")
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        content()
    }
}

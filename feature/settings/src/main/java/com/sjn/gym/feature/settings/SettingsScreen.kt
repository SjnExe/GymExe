package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.feature.settings.components.RestoreOptionsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle()
    val heightUnit by viewModel.heightUnit.collectAsStateWithLifecycle()
    val backupStatus by viewModel.backupStatus.collectAsStateWithLifecycle()
    val updateStatus by viewModel.updateStatus.collectAsStateWithLifecycle()

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
                } catch (
                    @Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception,
                ) {
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

    LaunchedEffect(updateStatus) {
        when (val status = updateStatus) {
            is UpdateStatus.NoUpdate -> {
                snackbarHostState.showSnackbar("No update available")
                viewModel.clearUpdateStatus()
            }

            is UpdateStatus.Error -> {
                snackbarHostState.showSnackbar(status.message)
                viewModel.clearUpdateStatus()
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
                } catch (
                    @Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception,
                ) {
                    // Handle error
                }
                showRestoreDialog = false
                restoreUri = null
            },
        )
    }

    // Loading overlay
    if (backupStatus is BackupStatus.Loading || updateStatus is UpdateStatus.Checking) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (updateStatus is UpdateStatus.Checking) "Checking for updates..." else "Processing...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
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
                    .verticalScroll(rememberScrollState()),
        ) {
            // Theme Section
            SettingsSectionTitle("Appearance")
            SelectionItem(
                title = "Theme",
                options = listOf("LIGHT", "DARK", "SYSTEM"),
                selectedOption = themeMode,
                onOptionSelected = { viewModel.setTheme(it) },
            )
            HorizontalDivider()

            // Units Section
            SettingsSectionTitle("Units")
            SelectionItem(
                title = "Weight Unit",
                options = listOf("KG", "LBS"),
                selectedOption = weightUnit,
                onOptionSelected = { viewModel.setWeightUnit(it) },
            )
            SelectionItem(
                title = "Height Unit",
                options = listOf("CM", "FT"),
                selectedOption = heightUnit,
                onOptionSelected = { viewModel.setHeightUnit(it) },
            )
            HorizontalDivider()

            // Data Section
            SettingsSectionTitle("Data")
            ListItem(
                headlineContent = { Text("Backup Data") },
                supportingContent = { Text("Save your data to a file") },
                leadingContent = { Icon(Icons.Default.Backup, contentDescription = null) },
                modifier = Modifier.clickable { createDocumentLauncher.launch("backup.gym") },
            )
            ListItem(
                headlineContent = { Text("Restore Data") },
                supportingContent = { Text("Import data from a file") },
                leadingContent = { Icon(Icons.Default.Restore, contentDescription = null) },
                modifier = Modifier.clickable { openDocumentLauncher.launch(arrayOf("*/*")) },
            )
            HorizontalDivider()

            // About Section
            SettingsSectionTitle("About")
            ListItem(
                headlineContent = { Text("Version") },
                supportingContent = { Text(viewModel.appVersion) },
            )
            ListItem(
                headlineContent = { Text("Check for Updates") },
                modifier = Modifier.clickable { viewModel.checkForUpdates() },
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
    )
}

@Composable
fun SelectionItem(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .clickable { onOptionSelected(option) }
                            .padding(4.dp),
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }
}

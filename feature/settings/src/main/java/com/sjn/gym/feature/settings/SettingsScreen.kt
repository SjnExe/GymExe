@file:Suppress("LongMethod", "CyclomaticComplexMethod")

package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.feature.settings.components.RestoreOptionsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    isDevMode: Boolean = false,
) {
    val state = rememberSettingsState(viewModel)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // State for restore flow
    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreUri by remember { mutableStateOf<Uri?>(null) }

    // State for dialogs
    var showThemeConfigDialog by remember { mutableStateOf(false) }
    var showThemeStyleDialog by remember { mutableStateOf(false) }
    var showWeightUnitDialog by remember { mutableStateOf(false) }
    var showHeightUnitDialog by remember { mutableStateOf(false) }
    var showDistanceUnitDialog by remember { mutableStateOf(false) }

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
                    e: Exception,
                ) {
                    // Handle error
                }
            }
        }

    val openDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
        ) { uri ->
            uri?.let {
                restoreUri = it
                showRestoreDialog = true
            }
        }

    HandleEffects(state.backupStatus, state.updateStatus, snackbarHostState, viewModel)

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
                    e: Exception,
                ) {
                    // Handle error
                }
                showRestoreDialog = false
                restoreUri = null
            },
        )
    }

    LoadingOverlay(state.backupStatus, state.updateStatus)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
        ) {
            AppearanceSection(
                state =
                    AppearanceState(
                        themeConfig = state.themeConfig,
                        themeStyle = state.themeStyle,
                        customThemeColor = state.customThemeColor,
                    ),
                onThemeConfigClick = { showThemeConfigDialog = true },
                onThemeStyleClick = { showThemeStyleDialog = true },
                onColorSelected = { viewModel.setCustomThemeColor(it) },
            )
            HorizontalDivider()

            UnitsSection(
                state =
                    UnitsState(
                        weightUnit = state.weightUnit,
                        heightUnit = state.heightUnit,
                        distanceUnit = state.distanceUnit,
                    ),
                onWeightUnitClick = { showWeightUnitDialog = true },
                onHeightUnitClick = { showHeightUnitDialog = true },
                onDistanceUnitClick = { showDistanceUnitDialog = true },
            )
            HorizontalDivider()

            DataSection(
                createDocumentLauncher = createDocumentLauncher,
                openDocumentLauncher = openDocumentLauncher,
            )
            HorizontalDivider()

            if (isDevMode) {
                DevelopmentSection(
                    onCopyLogs = { viewModel.copyLogs() },
                    onSaveLogs = { viewModel.saveLogs(context) },
                )
                HorizontalDivider()
            }

            AboutSection(
                appVersion = viewModel.appVersion,
                onCheckForUpdates = { viewModel.checkForUpdates() },
            )
        }
    }

    SettingsDialogs(
        state = state,
        showThemeConfigDialog = showThemeConfigDialog,
        showThemeStyleDialog = showThemeStyleDialog,
        showWeightUnitDialog = showWeightUnitDialog,
        showHeightUnitDialog = showHeightUnitDialog,
        showDistanceUnitDialog = showDistanceUnitDialog,
        onDismissThemeConfig = { showThemeConfigDialog = false },
        onDismissThemeStyle = { showThemeStyleDialog = false },
        onDismissWeightUnit = { showWeightUnitDialog = false },
        onDismissHeightUnit = { showHeightUnitDialog = false },
        onDismissDistanceUnit = { showDistanceUnitDialog = false },
        viewModel = viewModel,
    )
}

@Composable
@Suppress("LongParameterList")
fun SettingsDialogs(
    state: SettingsState,
    showThemeConfigDialog: Boolean,
    showThemeStyleDialog: Boolean,
    showWeightUnitDialog: Boolean,
    showHeightUnitDialog: Boolean,
    showDistanceUnitDialog: Boolean,
    onDismissThemeConfig: () -> Unit,
    onDismissThemeStyle: () -> Unit,
    onDismissWeightUnit: () -> Unit,
    onDismissHeightUnit: () -> Unit,
    onDismissDistanceUnit: () -> Unit,
    viewModel: SettingsViewModel,
) {
    if (showThemeConfigDialog) {
        SelectionDialog(
            title = "Theme",
            options = ThemeConfig.entries,
            selectedOption = state.themeConfig,
            onOptionSelected = { viewModel.setThemeConfig(it) },
            onDismissRequest = onDismissThemeConfig,
        )
    }
    if (showThemeStyleDialog) {
        SelectionDialog(
            title = "Style",
            options = ThemeStyle.entries,
            selectedOption = state.themeStyle,
            onOptionSelected = { viewModel.setThemeStyle(it) },
            onDismissRequest = onDismissThemeStyle,
        )
    }
    if (showWeightUnitDialog) {
        SelectionDialog(
            title = "Weight Unit",
            options = WeightUnit.entries,
            selectedOption = state.weightUnit,
            onOptionSelected = { viewModel.setWeightUnit(it) },
            onDismissRequest = onDismissWeightUnit,
        )
    }
    if (showHeightUnitDialog) {
        SelectionDialog(
            title = "Height Unit",
            options = HeightUnit.entries,
            selectedOption = state.heightUnit,
            onOptionSelected = { viewModel.setHeightUnit(it) },
            onDismissRequest = onDismissHeightUnit,
        )
    }
    if (showDistanceUnitDialog) {
        SelectionDialog(
            title = "Distance Unit",
            options = DistanceUnit.entries,
            selectedOption = state.distanceUnit,
            onOptionSelected = { viewModel.setDistanceUnit(it) },
            onDismissRequest = onDismissDistanceUnit,
        )
    }
}

@Composable
fun HandleEffects(
    backupStatus: BackupStatus,
    updateStatus: UpdateStatus,
    snackbarHostState: SnackbarHostState,
    viewModel: SettingsViewModel,
) {
    LaunchedEffect(backupStatus) {
        when (backupStatus) {
            is BackupStatus.Success -> {
                snackbarHostState.showSnackbar(backupStatus.message)
                viewModel.clearStatus()
            }

            is BackupStatus.Error -> {
                snackbarHostState.showSnackbar(backupStatus.message)
                viewModel.clearStatus()
            }

            else -> {}
        }
    }

    LaunchedEffect(updateStatus) {
        when (updateStatus) {
            is UpdateStatus.NoUpdate -> {
                snackbarHostState.showSnackbar("No update available")
                viewModel.clearUpdateStatus()
            }

            is UpdateStatus.Error -> {
                snackbarHostState.showSnackbar(updateStatus.message)
                viewModel.clearUpdateStatus()
            }

            else -> {}
        }
    }
}

@Composable
fun LoadingOverlay(
    backupStatus: BackupStatus,
    updateStatus: UpdateStatus,
) {
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
}

data class AppearanceState(
    val themeConfig: ThemeConfig,
    val themeStyle: ThemeStyle,
    val customThemeColor: Int?,
)

@Composable
fun AppearanceSection(
    state: AppearanceState,
    onThemeConfigClick: () -> Unit,
    onThemeStyleClick: () -> Unit,
    onColorSelected: (Int) -> Unit,
) {
    SettingsSectionTitle("Appearance")
    ListItem(
        headlineContent = { Text("Theme") },
        supportingContent = { Text(state.themeConfig.name) },
        modifier = Modifier.clickable { onThemeConfigClick() },
    )
    ListItem(
        headlineContent = { Text("Style") },
        supportingContent = { Text(state.themeStyle.name) },
        modifier = Modifier.clickable { onThemeStyleClick() },
    )

    if (state.themeStyle == ThemeStyle.CUSTOM) {
        ListItem(
            headlineContent = { Text("Custom Color") },
            supportingContent = {
                ColorPicker(
                    selectedColor = state.customThemeColor ?: android.graphics.Color.BLUE,
                    onColorSelected = onColorSelected,
                )
            },
        )
    }
}

data class UnitsState(
    val weightUnit: WeightUnit,
    val heightUnit: HeightUnit,
    val distanceUnit: DistanceUnit,
)

@Composable
fun UnitsSection(
    state: UnitsState,
    onWeightUnitClick: () -> Unit,
    onHeightUnitClick: () -> Unit,
    onDistanceUnitClick: () -> Unit,
) {
    SettingsSectionTitle("Units")
    ListItem(
        headlineContent = { Text("Weight Unit") },
        supportingContent = { Text(state.weightUnit.name) },
        modifier = Modifier.clickable { onWeightUnitClick() },
    )
    ListItem(
        headlineContent = { Text("Height Unit") },
        supportingContent = { Text(state.heightUnit.name) },
        modifier = Modifier.clickable { onHeightUnitClick() },
    )
    ListItem(
        headlineContent = { Text("Distance Unit") },
        supportingContent = { Text(state.distanceUnit.name) },
        modifier = Modifier.clickable { onDistanceUnitClick() },
    )
}

@Composable
fun DataSection(
    createDocumentLauncher: ManagedActivityResultLauncher<String, Uri?>,
    openDocumentLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>,
) {
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
}

@Composable
fun DevelopmentSection(
    onCopyLogs: () -> Unit,
    onSaveLogs: () -> Unit,
) {
    SettingsSectionTitle("Development")
    ListItem(
        headlineContent = { Text("Copy Logs") },
        supportingContent = { Text("Copy logs to clipboard") },
        modifier = Modifier.clickable { onCopyLogs() },
    )
    ListItem(
        headlineContent = { Text("Save Logs") },
        supportingContent = { Text("Save logs to file") },
        modifier = Modifier.clickable { onSaveLogs() },
    )
}

@Composable
fun AboutSection(
    appVersion: String,
    onCheckForUpdates: () -> Unit,
) {
    SettingsSectionTitle("About")
    ListItem(
        headlineContent = { Text("Version") },
        supportingContent = { Text(appVersion) },
    )
    ListItem(
        headlineContent = { Text("Check for Updates") },
        modifier = Modifier.clickable { onCheckForUpdates() },
    )
}

data class SettingsState(
    val themeConfig: ThemeConfig,
    val themeStyle: ThemeStyle,
    val customThemeColor: Int?,
    val weightUnit: WeightUnit,
    val heightUnit: HeightUnit,
    val distanceUnit: DistanceUnit,
    val backupStatus: BackupStatus,
    val updateStatus: UpdateStatus,
)

@Composable
fun rememberSettingsState(viewModel: SettingsViewModel): SettingsState {
    val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()
    val themeStyle by viewModel.themeStyle.collectAsStateWithLifecycle()
    val customThemeColor by viewModel.customThemeColor.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle()
    val heightUnit by viewModel.heightUnit.collectAsStateWithLifecycle()
    val distanceUnit by viewModel.distanceUnit.collectAsStateWithLifecycle()
    val backupStatus by viewModel.backupStatus.collectAsStateWithLifecycle()
    val updateStatus by viewModel.updateStatus.collectAsStateWithLifecycle()

    return remember(themeConfig, themeStyle, customThemeColor, weightUnit, heightUnit, distanceUnit, backupStatus, updateStatus) {
        SettingsState(themeConfig, themeStyle, customThemeColor, weightUnit, heightUnit, distanceUnit, backupStatus, updateStatus)
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
fun <T : Enum<T>> SelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOptionSelected(option)
                                    onDismissRequest()
                                }.padding(vertical = 12.dp),
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = {
                                onOptionSelected(option)
                                onDismissRequest()
                            },
                        )
                        Text(
                            text = option.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

object ThemeColors {
    val PALETTE =
        listOf(
            0xFFF44336, // Red
            0xFFE91E63, // Pink
            0xFF9C27B0, // Purple
            0xFF673AB7, // Deep Purple
            0xFF3F51B5, // Indigo
            0xFF2196F3, // Blue
            0xFF03A9F4, // Light Blue
            0xFF00BCD4, // Cyan
            0xFF009688, // Teal
            0xFF4CAF50, // Green
            0xFF8BC34A, // Light Green
            0xFFCDDC39, // Lime
            0xFFFFEB3B, // Yellow
            0xFFFFC107, // Amber
            0xFFFF9800, // Orange
            0xFFFF5722, // Deep Orange
            0xFF795548, // Brown
            0xFF9E9E9E, // Grey
            0xFF607D8B, // Blue Grey
        ).map { it.toInt() }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(
    selectedColor: Int,
    onColorSelected: (Int) -> Unit,
) {
    val colors = ThemeColors.PALETTE

    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        colors.forEach { colorInt ->
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(colorInt))
                        .border(
                            width = if (selectedColor == colorInt) 3.dp else 0.dp,
                            color = if (selectedColor == colorInt) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape,
                        ).clickable { onColorSelected(colorInt) },
            )
        }
    }
}

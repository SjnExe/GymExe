@file:Suppress("LongMethod", "CyclomaticComplexMethod", "LongParameterList", "TooManyFunctions")

package com.sjn.gym.feature.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.data.repository.DownloadStatus
import com.sjn.gym.core.data.repository.UpdateInfo
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.core.ui.components.RestoreOptionsDialog
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    isDevMode: Boolean = false,
    onNavigateUp: () -> Unit = {},
    onLaunchNetworkInspector: () -> Unit = {},
) {
    val state = rememberSettingsState(viewModel)
    SettingsScreenContent(
        state = state,
        appVersion = viewModel.appVersion,
        modifier = modifier,
        isDevMode = isDevMode,
        onNavigateUp = onNavigateUp,
        onLaunchNetworkInspector = onLaunchNetworkInspector,
        onThemeSelected = { viewModel.setThemeConfig(it) },
        onThemeStyleChanged = { viewModel.setThemeStyle(it) },
        onCustomThemeColorSelected = { viewModel.setCustomThemeColor(it) },
        onWeightUnitSelected = { viewModel.setWeightUnit(it) },
        onHeightUnitSelected = { viewModel.setHeightUnit(it) },
        onDistanceUnitSelected = { viewModel.setDistanceUnit(it) },
        onBackupRequested = { viewModel.performBackup(it) },
        onRestoreRequested = { inputStream, options -> viewModel.restoreBackup(inputStream, options) },
        onCheckForUpdates = { viewModel.checkForUpdates() },
        onDownloadUpdate = { viewModel.downloadUpdate(it) },
        onClearBackupStatus = { viewModel.clearStatus() },
        onClearUpdateStatus = { viewModel.clearUpdateStatus() },
        onCopyLogs = { viewModel.copyLogs() },
        onSaveLogs = { viewModel.saveLogs(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    state: SettingsState,
    appVersion: String,
    modifier: Modifier = Modifier,
    isDevMode: Boolean = false,
    onNavigateUp: () -> Unit = {},
    onLaunchNetworkInspector: () -> Unit = {},
    onThemeSelected: (ThemeConfig) -> Unit = {},
    onThemeStyleChanged: (ThemeStyle) -> Unit = {},
    onCustomThemeColorSelected: (Int) -> Unit = {},
    onWeightUnitSelected: (WeightUnit) -> Unit = {},
    onHeightUnitSelected: (HeightUnit) -> Unit = {},
    onDistanceUnitSelected: (DistanceUnit) -> Unit = {},
    onBackupRequested: (java.io.OutputStream) -> Unit = {},
    onRestoreRequested: (java.io.InputStream, com.sjn.gym.core.model.backup.RestoreOptions) -> Unit = { _, _ -> },
    onCheckForUpdates: () -> Unit = {},
    onDownloadUpdate: (String) -> Unit = {},
    onClearBackupStatus: () -> Unit = {},
    onClearUpdateStatus: () -> Unit = {},
    onCopyLogs: () -> Unit = {},
    onSaveLogs: (android.content.Context) -> Unit = {},
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // State for restore flow
    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreUri by remember { mutableStateOf<Uri?>(null) }

    val createDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
        ) { uri ->
            uri?.let {
                try {
                    context.contentResolver.openOutputStream(it)?.let { outputStream ->
                        onBackupRequested(outputStream)
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

    HandleEffects(
        backupStatus = state.backupStatus,
        updateStatus = state.updateStatus,
        downloadStatus = state.downloadStatus,
        snackbarHostState = snackbarHostState,
        onClearBackupStatus = onClearBackupStatus,
        onClearUpdateStatus = onClearUpdateStatus,
    )

    if (showRestoreDialog && restoreUri != null) {
        RestoreOptionsDialog(
            onDismissRequest = {
                showRestoreDialog = false
                restoreUri = null
            },
            onConfirm = { options ->
                try {
                    context.contentResolver.openInputStream(restoreUri!!)?.let { inputStream ->
                        onRestoreRequested(inputStream, options)
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

    if (state.updateStatus is UpdateStatus.UpdateAvailable) {
        val updateInfo = state.updateStatus.updateInfo
        UpdateDialog(
            currentVersion = appVersion,
            updateInfo = updateInfo,
            downloadStatus = state.downloadStatus,
            onDismissRequest = onClearUpdateStatus,
            onUpdate = { onDownloadUpdate(updateInfo.downloadUrl) },
        )
    }

    LoadingOverlay(state.backupStatus, state.updateStatus)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
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
            SettingsSectionHeader("Theme")

            ThemeSelectionRow(
                themeConfig = state.themeConfig,
                onThemeSelected = onThemeSelected,
            )

            SettingsSwitchRow(
                title = "Dynamic Colors (Material You)",
                checked = state.themeStyle == ThemeStyle.DYNAMIC,
                onCheckedChange = { checked ->
                    onThemeStyleChanged(if (checked) ThemeStyle.DYNAMIC else ThemeStyle.CUSTOM)
                },
                icon = Icons.Default.ColorLens,
            )

            AnimatedVisibility(
                visible = state.themeStyle != ThemeStyle.DYNAMIC,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                ColorPicker(
                    selectedColor = state.customThemeColor ?: android.graphics.Color.BLUE,
                    onColorSelected = onCustomThemeColorSelected,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Units Section
            SettingsSectionHeader("Units")

            SettingsUnitRow(
                title = "Weight Unit",
                options = WeightUnit.entries,
                selectedOption = state.weightUnit,
                onOptionSelected = onWeightUnitSelected,
            )

            SettingsUnitRow(
                title = "Height Unit",
                options = HeightUnit.entries,
                selectedOption = state.heightUnit,
                onOptionSelected = onHeightUnitSelected,
            )

            SettingsUnitRow(
                title = "Distance Unit",
                options = DistanceUnit.entries,
                selectedOption = state.distanceUnit,
                onOptionSelected = onDistanceUnitSelected,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Data Section
            SettingsSectionHeader("Data Management")
            SettingsActionRow(
                title = "Backup Data",
                subtitle = "Save your data to a file",
                icon = Icons.Default.Backup,
                onClick = { createDocumentLauncher.launch("backup.gym") },
            )
            SettingsActionRow(
                title = "Restore Data",
                subtitle = "Import data from a file",
                icon = Icons.Default.Restore,
                onClick = { openDocumentLauncher.launch(arrayOf("*/*")) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Developer Options
            if (isDevMode) {
                SettingsSectionHeader("Developer Options")
                SettingsActionRow(
                    title = "Open Network Inspector",
                    icon = Icons.Default.BugReport,
                    onClick = onLaunchNetworkInspector,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    FilledTonalButton(
                        onClick = onCopyLogs,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Copy Logs")
                    }
                    FilledTonalButton(
                        onClick = { onSaveLogs(context) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Save Logs")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // About Section (Always visible)
            AboutSection(
                appVersion = appVersion,
                onCheckForUpdates = onCheckForUpdates,
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun UpdateDialog(
    currentVersion: String,
    updateInfo: UpdateInfo,
    downloadStatus: DownloadStatus,
    onDismissRequest: () -> Unit,
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = { if (downloadStatus !is DownloadStatus.Downloading) onDismissRequest() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Update Available",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            text = "Current: $currentVersion",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "To")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "New: ${updateInfo.version}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (updateInfo.architecture != null) {
                        AssistChip(
                            onClick = {},
                            label = { Text(updateInfo.architecture ?: "") },
                            modifier = Modifier.height(24.dp),
                        )
                    }
                }

                if (updateInfo.isStable) {
                    Text(
                        text = "(Stable Release)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Limited release notes
                val notes =
                    if (updateInfo.releaseNotes.length > 200) {
                        updateInfo.releaseNotes.take(200) + "..."
                    } else {
                        updateInfo.releaseNotes
                    }
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (downloadStatus is DownloadStatus.Downloading) {
                    val progress = downloadStatus.progress
                    val downloaded = formatFileSize(downloadStatus.downloadedBytes)
                    val total = formatFileSize(downloadStatus.totalBytes)
                    val statusText =
                        if (downloadStatus.totalBytes > 0) {
                            "Downloading... $downloaded / $total ($progress%)"
                        } else {
                            "Downloading... $downloaded"
                        }

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { if (progress >= 0) progress / 100f else 0f },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        androidx.compose.material3.TextButton(onClick = onDismissRequest) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledTonalButton(onClick = onUpdate) {
                            Text("Download & Install")
                        }
                    }
                }
            }
        }
    }
}

private fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(
        Locale.US,
        "%.1f %s",
        bytes / Math.pow(1024.0, digitGroups.toDouble()),
        units[digitGroups],
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> SettingsUnitRow(
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { onOptionSelected(option) },
                    selected = option == selectedOption,
                    label = { Text(option.name) },
                )
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionRow(
    themeConfig: ThemeConfig,
    onThemeSelected: (ThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(ThemeConfig.SYSTEM, ThemeConfig.LIGHT, ThemeConfig.DARK)
    val labels = listOf("System", "Light", "Dark")
    val selectedIndex = options.indexOf(themeConfig)

    SingleChoiceSegmentedButtonRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onThemeSelected(option) },
                selected = index == selectedIndex,
                label = { Text(labels[index]) },
            )
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun SettingsDetailRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SettingsActionRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun HandleEffects(
    backupStatus: BackupStatus,
    updateStatus: UpdateStatus,
    downloadStatus: DownloadStatus,
    snackbarHostState: SnackbarHostState,
    onClearBackupStatus: () -> Unit,
    onClearUpdateStatus: () -> Unit,
) {
    LaunchedEffect(backupStatus) {
        when (backupStatus) {
            is BackupStatus.Success -> {
                snackbarHostState.showSnackbar(backupStatus.message)
                onClearBackupStatus()
            }

            is BackupStatus.Error -> {
                snackbarHostState.showSnackbar(backupStatus.message)
                onClearBackupStatus()
            }

            else -> {}
        }
    }

    LaunchedEffect(updateStatus) {
        when (updateStatus) {
            is UpdateStatus.NoUpdate -> {
                snackbarHostState.showSnackbar("No update available")
                onClearUpdateStatus()
            }

            is UpdateStatus.Error -> {
                snackbarHostState.showSnackbar(updateStatus.message)
                onClearUpdateStatus()
            }

            else -> {}
        }
    }

    LaunchedEffect(downloadStatus) {
        if (downloadStatus is DownloadStatus.Error) {
            snackbarHostState.showSnackbar(downloadStatus.message)
            // We don't clear status immediately so dialog stays open?
            // Actually dialog relies on updateStatus too.
        }
    }
}

@Composable
fun LoadingOverlay(
    backupStatus: BackupStatus,
    updateStatus: UpdateStatus,
    modifier: Modifier = Modifier,
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

@Composable
fun AboutSection(
    appVersion: String,
    onCheckForUpdates: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingsSectionTitle("About")
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Version",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = appVersion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            FilledTonalButton(onClick = onCheckForUpdates) {
                Text("Check for Updates")
            }
        }
    }
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
    val downloadStatus: DownloadStatus,
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
    val downloadStatus by viewModel.downloadStatus.collectAsStateWithLifecycle()

    return remember(
        themeConfig,
        themeStyle,
        customThemeColor,
        weightUnit,
        heightUnit,
        distanceUnit,
        backupStatus,
        updateStatus,
        downloadStatus,
    ) {
        SettingsState(
            themeConfig,
            themeStyle,
            customThemeColor,
            weightUnit,
            heightUnit,
            distanceUnit,
            backupStatus,
            updateStatus,
            downloadStatus,
        )
    }
}

@Composable
fun SettingsSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
    )
}

@Composable
fun <T : Enum<T>> SelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
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
    modifier: Modifier = Modifier,
) {
    val colors = ThemeColors.PALETTE

    FlowRow(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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

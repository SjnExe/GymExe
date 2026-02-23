@file:Suppress("LongMethod", "CyclomaticComplexMethod", "LongParameterList", "TooManyFunctions")

package com.sjn.gym.feature.settings

import android.content.Intent
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.data.repository.UpdateInfo
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.core.ui.components.RestoreOptionsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    isDevMode: Boolean = false,
    onNavigateUp: () -> Unit = {},
    onLaunchNetworkInspector: () -> Unit = {},
) {
    val state = rememberSettingsState(viewModel)
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

    if (state.updateStatus is UpdateStatus.UpdateAvailable) {
        val updateInfo = (state.updateStatus as UpdateStatus.UpdateAvailable).updateInfo
        UpdateDialog(
            updateInfo = updateInfo,
            onDismissRequest = { viewModel.clearUpdateStatus() },
            onUpdate = {
                // Launch browser download
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateInfo.downloadUrl))
                context.startActivity(intent)
                viewModel.clearUpdateStatus()
            },
        )
    }

    LoadingOverlay(state.backupStatus, state.updateStatus)

    Scaffold(
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
                onThemeSelected = { viewModel.setThemeConfig(it) },
            )

            SettingsSwitchRow(
                title = "Dynamic Colors (Material You)",
                checked = state.themeStyle == ThemeStyle.DYNAMIC,
                onCheckedChange = { checked ->
                    viewModel.setThemeStyle(if (checked) ThemeStyle.DYNAMIC else ThemeStyle.CUSTOM)
                },
            )

            AnimatedVisibility(
                visible = state.themeStyle != ThemeStyle.DYNAMIC,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                ColorPicker(
                    selectedColor = state.customThemeColor ?: android.graphics.Color.BLUE,
                    onColorSelected = { viewModel.setCustomThemeColor(it) },
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Units Section
            SettingsSectionHeader("Units")

            SettingsUnitRow(
                title = "Weight Unit",
                options = WeightUnit.entries,
                selectedOption = state.weightUnit,
                onOptionSelected = { viewModel.setWeightUnit(it) },
            )

            SettingsUnitRow(
                title = "Height Unit",
                options = HeightUnit.entries,
                selectedOption = state.heightUnit,
                onOptionSelected = { viewModel.setHeightUnit(it) },
            )

            SettingsUnitRow(
                title = "Distance Unit",
                options = DistanceUnit.entries,
                selectedOption = state.distanceUnit,
                onOptionSelected = { viewModel.setDistanceUnit(it) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Data Section
            SettingsSectionHeader("Data Management")
            SettingsActionRow(
                title = "Backup Data",
                subtitle = "Save your data to a file",
                onClick = { createDocumentLauncher.launch("backup.gym") },
            )
            SettingsActionRow(
                title = "Restore Data",
                subtitle = "Import data from a file",
                onClick = { openDocumentLauncher.launch(arrayOf("*/*")) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Developer Options
            if (isDevMode) {
                SettingsSectionHeader("Developer Options")
                SettingsActionRow(
                    title = "Open Network Inspector",
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
                        onClick = { viewModel.copyLogs() },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Copy Logs")
                    }
                    FilledTonalButton(
                        onClick = { viewModel.saveLogs(context) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Save Logs")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // If not dev mode, show version info here
                AboutSection(
                    appVersion = viewModel.appVersion,
                    onCheckForUpdates = { viewModel.checkForUpdates() },
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun UpdateDialog(
    updateInfo: UpdateInfo,
    onDismissRequest: () -> Unit,
    onUpdate: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
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
                Text(
                    text = "New Version: ${updateInfo.version}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledTonalButton(onClick = onUpdate) {
                        Text("Download")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> SettingsUnitRow(
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
) {
    Column(
        modifier =
            Modifier
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
fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionRow(
    themeConfig: ThemeConfig,
    onThemeSelected: (ThemeConfig) -> Unit,
) {
    val options = listOf(ThemeConfig.SYSTEM, ThemeConfig.LIGHT, ThemeConfig.DARK)
    val labels = listOf("System", "Light", "Dark")
    val selectedIndex = options.indexOf(themeConfig)

    SingleChoiceSegmentedButtonRow(
        modifier =
            Modifier
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
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
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
) {
    Row(
        modifier =
            Modifier
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
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
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

@Composable
fun AboutSection(
    appVersion: String,
    onCheckForUpdates: () -> Unit,
) {
    SettingsSectionTitle("About")
    SettingsDetailRow(
        title = "Version",
        value = appVersion,
        onClick = {},
    )
    SettingsActionRow(
        title = "Check for Updates",
        onClick = onCheckForUpdates,
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
        style = MaterialTheme.typography.labelMedium,
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
        modifier =
            Modifier
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

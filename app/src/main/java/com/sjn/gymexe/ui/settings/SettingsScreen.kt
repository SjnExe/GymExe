@file:Suppress("MagicNumber")

package com.sjn.gymexe.ui.settings

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sjn.gymexe.domain.manager.DownloadStatus
import com.sjn.gymexe.domain.manager.UpdateResult
import com.sjn.gymexe.ui.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

private val PADDING_MEDIUM = 16.dp
private val PADDING_SMALL = 8.dp

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val weightUnit by viewModel.weightUnit.collectAsState()
    val distanceUnit by viewModel.distanceUnit.collectAsState()
    val downloadStatus by viewModel.downloadStatus.collectAsState()
    val context = LocalContext.current

    var showUpdateDialog by remember { mutableStateOf<UpdateResult.UpdateAvailable?>(null) }

    LaunchedEffect(Unit) {
        viewModel.updateEvents.collect { event ->
            when (event) {
                is SettingsViewModel.UpdateEvent.Checking -> {
                    Toast.makeText(context, "Checking for updates...", Toast.LENGTH_SHORT).show()
                }
                is SettingsViewModel.UpdateEvent.NoUpdate -> {
                    Toast.makeText(context, "No update available", Toast.LENGTH_SHORT).show()
                }
                is SettingsViewModel.UpdateEvent.UpdateAvailable -> {
                    showUpdateDialog = event.update
                }
                is SettingsViewModel.UpdateEvent.Error -> {
                    Toast.makeText(context, "Update check failed: ${event.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    UpdateDialogs(
        showUpdateDialog = showUpdateDialog,
        downloadStatus = downloadStatus,
        onDismissUpdateDialog = { showUpdateDialog = null },
        onDownload = { url, version ->
            viewModel.downloadUpdate(url, version)
            showUpdateDialog = null
        },
        onDownloadComplete = { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(uri), "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)
            viewModel.resetDownloadStatus()
        }
    )

    SettingsContent(
        state = SettingsState(themeMode, useDynamicColors, weightUnit, distanceUnit),
        actions = SettingsActions(
            onThemeModeChange = viewModel::setThemeMode,
            onDynamicColorChange = viewModel::setUseDynamicColors,
            onWeightUnitChange = viewModel::setWeightUnit,
            onDistanceUnitChange = viewModel::setDistanceUnit,
            onCheckUpdate = viewModel::checkForUpdates
        )
    )
}

data class SettingsState(
    val themeMode: String,
    val useDynamicColors: Boolean,
    val weightUnit: String,
    val distanceUnit: String
)

data class SettingsActions(
    val onThemeModeChange: (String) -> Unit,
    val onDynamicColorChange: (Boolean) -> Unit,
    val onWeightUnitChange: (String) -> Unit,
    val onDistanceUnitChange: (String) -> Unit,
    val onCheckUpdate: () -> Unit
)

@Composable
private fun SettingsContent(
    state: SettingsState,
    actions: SettingsActions
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM),
        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM),
    ) {
        item {
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            ThemeSettingsCard(
                themeMode = state.themeMode,
                useDynamicColors = state.useDynamicColors,
                onThemeModeChange = actions.onThemeModeChange,
                onDynamicColorChange = actions.onDynamicColorChange,
            )
        }
        item {
            UnitSettingsCard(
                weightUnit = state.weightUnit,
                distanceUnit = state.distanceUnit,
                onWeightUnitChange = actions.onWeightUnitChange,
                onDistanceUnitChange = actions.onDistanceUnitChange,
            )
        }
        item { HorizontalDivider() }
        item {
            Text(
                text = "System",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            SystemActionsCard(
                context = context,
                onCheckUpdate = actions.onCheckUpdate
            )
        }
    }
}

// ... UpdateDialogs ...
@Composable
fun UpdateDialogs(
    showUpdateDialog: UpdateResult.UpdateAvailable?,
    downloadStatus: DownloadStatus,
    onDismissUpdateDialog: () -> Unit,
    onDownload: (String, String) -> Unit,
    onDownloadComplete: (String) -> Unit
) {
    if (showUpdateDialog != null) {
        AlertDialog(
            onDismissRequest = onDismissUpdateDialog,
            title = { Text("Update Available") },
            text = { Text("Version ${showUpdateDialog.version} is available. Download now?") },
            confirmButton = {
                TextButton(onClick = {
                    onDownload(showUpdateDialog.url, showUpdateDialog.version)
                }) { Text("Download") }
            },
            dismissButton = {
                TextButton(onClick = onDismissUpdateDialog) { Text("Cancel") }
            }
        )
    }

    if (downloadStatus is DownloadStatus.Downloading) {
        val progress = downloadStatus.progress
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Downloading Update") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(progress = { progress })
                    Spacer(modifier = Modifier.height(PADDING_SMALL))
                    Text("${(progress * 100).toInt()}%")
                }
            },
            confirmButton = {}
        )
    }

    LaunchedEffect(downloadStatus) {
        if (downloadStatus is DownloadStatus.Completed) {
             onDownloadComplete(downloadStatus.fileUri)
        }
    }
}

@Composable
fun ThemeSettingsCard(
    themeMode: String,
    useDynamicColors: Boolean,
    onThemeModeChange: (String) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(PADDING_MEDIUM)) {
            Text("Theme Mode", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(PADDING_SMALL)) // Changed 8.dp to PADDING_SMALL

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                val options = listOf("System", "Light", "Dark")
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { onThemeModeChange(label) },
                        selected = themeMode == label,
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(PADDING_MEDIUM))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text("Use Material You", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Dynamic colors from wallpaper",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = useDynamicColors,
                    onCheckedChange = onDynamicColorChange,
                )
            }
        }
    }
}

@Composable
fun UnitSettingsCard(
    weightUnit: String,
    distanceUnit: String,
    onWeightUnitChange: (String) -> Unit,
    onDistanceUnitChange: (String) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(PADDING_MEDIUM)) {
            Text("Units", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(PADDING_MEDIUM))

            // Weight Unit
            Text("Weight", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(PADDING_SMALL)) // Changed 8.dp
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                val options = listOf("KG", "LBS")
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { onWeightUnitChange(label) },
                        selected = weightUnit == label,
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(PADDING_MEDIUM))

            // Distance Unit
            Text("Distance", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(PADDING_SMALL)) // Changed 8.dp
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                val options = listOf("KM", "MILES")
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { onDistanceUnitChange(label) },
                        selected = distanceUnit == label,
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

@Composable
fun SystemActionsCard(
    context: Context,
    onCheckUpdate: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val exportLogLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                saveLogsToFile(context, it)
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(PADDING_MEDIUM)) {
            Button(
                onClick = onCheckUpdate,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Check for Updates")
            }

            Spacer(modifier = Modifier.height(PADDING_SMALL)) // Changed 8.dp

            Button(
                onClick = {
                    scope.launch {
                        copyLogsToClipboard(context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Copy Debug Logs")
            }

            Spacer(modifier = Modifier.height(PADDING_SMALL)) // Changed 8.dp

            Button(
                onClick = {
                   exportLogLauncher.launch("GymExe_Log_${System.currentTimeMillis()}.log")
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save Debug Logs")
            }
        }
    }
}

@Suppress("TooGenericExceptionCaught")
private suspend fun getLogcatOutput(): String {
    return withContext(Dispatchers.IO) {
        try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val log = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                log.append(line).append("\n")
            }
            log.toString()
        } catch (e: Exception) {
            "Failed to retrieve logs: ${e.message}"
        }
    }
}

private suspend fun copyLogsToClipboard(context: Context) {
    val logs = getLogcatOutput()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("GymExe Debug Logs", logs)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Logs copied to clipboard", Toast.LENGTH_SHORT).show()
}

@Suppress("TooGenericExceptionCaught")
private suspend fun saveLogsToFile(context: Context, uri: Uri) {
    withContext(Dispatchers.IO) {
        try {
            val logs = getLogcatOutput()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(logs.toByteArray())
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Logs saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save logs: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

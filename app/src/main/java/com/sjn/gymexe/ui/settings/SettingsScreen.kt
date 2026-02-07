package com.sjn.gymexe.ui.settings

import android.app.Activity
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sjn.gymexe.ui.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val weightUnit by viewModel.weightUnit.collectAsState()
    val distanceUnit by viewModel.distanceUnit.collectAsState()
    val context = LocalContext.current

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
                    Toast.makeText(context, "Downloading update: ${event.update.version}", Toast.LENGTH_LONG).show()
                }
                is SettingsViewModel.UpdateEvent.Error -> {
                    Toast.makeText(context, "Update check failed: ${event.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Preferences", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }

        item {
            ThemeSettingsCard(
                themeMode = themeMode,
                useDynamicColors = useDynamicColors,
                onThemeModeChange = viewModel::setThemeMode,
                onDynamicColorChange = viewModel::setUseDynamicColors,
            )
        }

        item {
            UnitSettingsCard(
                weightUnit = weightUnit,
                distanceUnit = distanceUnit,
                onWeightUnitChange = viewModel::setWeightUnit,
                onDistanceUnitChange = viewModel::setDistanceUnit,
            )
        }

        item {
            HorizontalDivider()
        }

        item {
            Text("System", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }

        item {
            SystemActionsCard(
                context = context,
                onCheckUpdate = viewModel::checkForUpdates
            )
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Theme Mode", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Units", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Weight Unit
            Text("Weight", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
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

            Spacer(modifier = Modifier.height(16.dp))

            // Distance Unit
            Text("Distance", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
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
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = onCheckUpdate,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Check for Updates")
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

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

package com.sjn.gymexe.ui.settings

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sjn.gymexe.ui.settings.viewmodel.SettingsViewModel
import java.io.File

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val useMetricDisplay by viewModel.useMetricDisplay.collectAsState()
    val context = LocalContext.current

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
                useMetric = useMetricDisplay,
                onUnitChange = viewModel::setUseMetricDisplay,
            )
        }

        item {
            HorizontalDivider()
        }

        item {
            Text("System", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        }

        item {
            SystemActionsCard(context = context)
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
    useMetric: Boolean,
    onUnitChange: (Boolean) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Display Units", style = MaterialTheme.typography.titleMedium)
                Text(
                    if (useMetric) "Metric (kg, km)" else "Imperial (lbs, miles)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Switch(
                checked = useMetric,
                onCheckedChange = onUnitChange,
            )
        }
    }
}

@Composable
fun SystemActionsCard(context: Context) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    Toast.makeText(context, "Checking for updates...", Toast.LENGTH_SHORT).show()
                    // TODO: Implement actual update check
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Check for Updates")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val logFile = File(context.cacheDir, "app_logs.txt")
                    logFile.writeText("Mock Log Data: Error 404\nWarning: Low Battery")
                    Toast.makeText(context, "Logs saved to Cache", Toast.LENGTH_SHORT).show()
                    // TODO: Implement actual log dump and share intent
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Copy Debug Logs")
            }
        }
    }
}

package com.sjn.gym.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjn.gym.core.model.backup.RestoreOptions

@Composable
fun RestoreOptionsDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (RestoreOptions) -> Unit,
) {
    var restoreExercises by remember { mutableStateOf(true) }
    var restoreWorkouts by remember { mutableStateOf(true) }
    var restoreProfile by remember { mutableStateOf(true) }
    var restoreSettings by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Restore Options") },
        text = {
            androidx.compose.foundation.layout.Column(
                verticalArrangement =
                    androidx.compose.foundation.layout.Arrangement
                        .spacedBy(8.dp),
            ) {
                Text(
                    "Select data to restore.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                OptionCheckbox("Exercises", restoreExercises, { restoreExercises = it })
                OptionCheckbox("Workouts", restoreWorkouts, { restoreWorkouts = it })
                OptionCheckbox("Profile", restoreProfile, { restoreProfile = it })
                OptionCheckbox("Settings", restoreSettings, { restoreSettings = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        RestoreOptions(
                            restoreExercises = restoreExercises,
                            restoreWorkouts = restoreWorkouts,
                            restoreProfile = restoreProfile,
                            restoreSettings = restoreSettings,
                        ),
                    )
                },
            ) {
                Text("Restore")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
    )
}

@Composable
fun OptionCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(vertical = 4.dp),
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

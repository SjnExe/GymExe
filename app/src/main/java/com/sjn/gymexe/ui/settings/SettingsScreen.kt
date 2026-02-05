package com.sjn.gymexe.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        // Theme
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Use Material You", modifier = Modifier.weight(1f))
            Switch(checked = true, onCheckedChange = {}) // TODO: Implement logic
        }

        // Units
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Units: Metric (SI)", modifier = Modifier.weight(1f))
            Button(onClick = {}) { Text("Change") } // TODO
        }

        // Update
        Button(onClick = {}, modifier = Modifier.padding(top = 16.dp)) {
            Text("Check for Updates")
        }
    }
}

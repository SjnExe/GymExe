package com.gym.exe.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gym.exe.BuildConfig

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.FLAVOR})")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SjnExe/GymExe/issues/new"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Send Feedback")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Backup Logic would go here
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Backup Data")
        }
    }
}

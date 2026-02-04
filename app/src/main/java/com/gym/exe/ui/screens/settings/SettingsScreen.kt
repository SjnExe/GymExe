package com.gym.exe.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    Column {
        Text("Settings")

        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SjnExe/GymExe/issues/new"))
            context.startActivity(intent)
        }) {
            Text("Send Feedback")
        }

        Button(onClick = {
            // Backup Logic would go here
        }) {
            Text("Backup Data")
        }
    }
}

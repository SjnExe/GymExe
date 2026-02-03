package com.sjn.gymexe.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

private const val MILLIS_IN_SECOND = 1000L
private const val SECONDS_IN_MINUTE = 60
private val OverlayPadding = 16.dp
private val ContentPadding = 16.dp

@Composable
fun RestTimerOverlay(remainingMillis: Long) {
    if (remainingMillis > 0) {
        val seconds = (remainingMillis / MILLIS_IN_SECOND) % SECONDS_IN_MINUTE
        val minutes = (remainingMillis / MILLIS_IN_SECOND) / SECONDS_IN_MINUTE

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(OverlayPadding),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(ContentPadding)) {
                Text(
                    text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

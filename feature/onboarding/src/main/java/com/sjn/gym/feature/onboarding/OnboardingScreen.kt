package com.sjn.gym.feature.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sjn.gym.core.ui.components.RestoreOptionsDialog

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val restoreState by viewModel.restoreStatus.collectAsState()
    val context = LocalContext.current

    var showRestoreDialog by remember { mutableStateOf(false) }
    var restoreUri by remember { mutableStateOf<Uri?>(null) }

    val openDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
        ) { uri ->
            uri?.let {
                restoreUri = it
                showRestoreDialog = true
            }
        }

    LaunchedEffect(restoreState) {
        if (restoreState is RestoreUiState.Success) {
            onOnboardingComplete()
            viewModel.resetRestoreStatus()
        }
    }

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
                } catch (e: Exception) {
                    // Handle error
                }
                showRestoreDialog = false
                restoreUri = null
            },
        )
    }

    if (restoreState is RestoreUiState.Loading) {
        Dialog(onDismissRequest = {}) {
            Surface {
                CircularProgressIndicator()
            }
        }
    }

    if (restoreState is RestoreUiState.Error) {
        AlertDialog(
            onDismissRequest = { viewModel.resetRestoreStatus() },
            title = { Text("Error") },
            text = { Text((restoreState as RestoreUiState.Error).message) },
            confirmButton = {
                TextButton(onClick = { viewModel.resetRestoreStatus() }) {
                    Text("OK")
                }
            },
        )
    }

    Box(modifier = modifier.systemBarsPadding()) {
        when (currentStep) {
            0 -> {
                WelcomeScreen(
                    onNewUser = { viewModel.nextStep() },
                    onRestoreBackup = { openDocumentLauncher.launch(arrayOf("*/*")) },
                )
            }

            1 -> {
                ProfileSetupScreen(onNext = { viewModel.nextStep() })
            }

            2 -> {
                ExperienceLevelScreen(onNext = { viewModel.nextStep() })
            }

            3 -> {
                EquipmentSelectionScreen(onComplete = onOnboardingComplete)
            }
        }
    }
}

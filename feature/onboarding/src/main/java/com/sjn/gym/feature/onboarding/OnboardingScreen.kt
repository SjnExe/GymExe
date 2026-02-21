package com.sjn.gym.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()

    when (currentStep) {
        0 -> ProfileSetupScreen(onNext = { viewModel.nextStep() })
        1 -> ExperienceLevelScreen(onNext = { viewModel.nextStep() })
        2 -> EquipmentSelectionScreen(onComplete = onOnboardingComplete)
    }
}

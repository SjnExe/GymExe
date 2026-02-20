package com.sjn.gym.feature.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    Column {
        Text("Welcome to GymExe")
        Button(onClick = {
            viewModel.completeOnboarding()
            onOnboardingComplete()
        }) {
            Text("Complete Setup")
        }
    }
}

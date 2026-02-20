package com.sjn.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sjn.gym.core.ui.theme.GymExeTheme
import com.sjn.gym.navigation.GymExeNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is MainActivityUiState.Loading -> {
                    // Placeholder loading screen
                    Surface(modifier = Modifier.fillMaxSize()) {
                        // Empty surface or splash
                    }
                }
                is MainActivityUiState.Success -> {
                    val darkTheme = when (state.userData.themeMode) {
                        "DARK" -> true
                        "LIGHT" -> false
                        else -> isSystemInDarkTheme()
                    }

                    GymExeTheme(
                        darkTheme = darkTheme,
                        dynamicColor = state.userData.useDynamicColor
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            GymExeNavHost(
                                isOnboardingCompleted = state.userData.isOnboardingCompleted
                            )
                        }
                    }
                }
            }
        }
    }
}

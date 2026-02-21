package com.sjn.gym

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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

            // Handle intent (e.g., opening a .gym file)
            // Ideally, this should be passed to the ViewModel or NavHost to trigger a dialog or navigation
            // For now, we'll just log or prepare it.
            // In a real implementation, you might want to use a SideEffect or a specific Intent Handler

            val intentUri = intent?.data

            when (val state = uiState) {
                is MainActivityUiState.Loading -> {
                    // Placeholder loading screen
                    Surface(modifier = Modifier.fillMaxSize()) {
                        // Empty surface or splash
                    }
                }
                is MainActivityUiState.Success -> {
                    // Determine theme
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

                    // TODO: If intentUri is present, trigger restore dialog in Settings or a global restore dialog.
                    // This requires moving the Restore Dialog to a higher level or navigating to Settings with an argument.
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle new intent if activity is already running
    }
}

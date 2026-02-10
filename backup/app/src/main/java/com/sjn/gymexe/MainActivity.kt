package com.sjn.gymexe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sjn.gymexe.ui.MainViewModel
import com.sjn.gymexe.ui.navigation.MainScreen
import com.sjn.gymexe.ui.theme.GymExeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val useDynamicColors by viewModel.useDynamicColors.collectAsState()

            val isDarkTheme =
                when (themeMode) {
                    "Light" -> false
                    "Dark" -> true
                    else -> isSystemInDarkTheme()
                }

            GymExeTheme(
                darkTheme = isDarkTheme,
                dynamicColor = useDynamicColors,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

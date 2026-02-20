package com.sjn.gymexe.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Pure Black for Dark Mode
private val PureBlack = Color(0xFF000000)
private val DarkSurface = Color(0xFF121212) // Fallback or Card color

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFD0BCFF),
        secondary = Color(0xFFCCC2DC),
        tertiary = Color(0xFFEFB8C8),
        background = PureBlack,
        surface = DarkSurface,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF6650a4),
        secondary = Color(0xFF625b71),
        tertiary = Color(0xFF7d5260),
    )

@Composable
fun GymExeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) {
                    // Force Pure Black background even with Dynamic Colors if desired
                    dynamicDarkColorScheme(context).copy(background = PureBlack)
                } else {
                    dynamicLightColorScheme(context)
                }
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

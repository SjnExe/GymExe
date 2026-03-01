package com.sjn.gym.feature.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.sjn.gym.core.data.repository.DownloadStatus
import com.sjn.gym.core.model.DistanceUnit
import com.sjn.gym.core.model.HeightUnit
import com.sjn.gym.core.model.ThemeConfig
import com.sjn.gym.core.model.ThemeStyle
import com.sjn.gym.core.model.WeightUnit
import com.sjn.gym.core.ui.theme.GymExeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], manifest = Config.NONE)
class SettingsScreenScreenshotTest {
    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @get:Rule(order = 2)
    val roborazziRule =
        RoborazziRule(
            composeRule,
            captureRoot = composeRule.onRoot(),
            options =
                RoborazziRule.Options(
                    outputDirectoryPath = "src/test/snapshots/images",
                ),
        )

    @Test
    fun testSettingsScreen() {
        composeRule.setContent {
            GymExeTheme {
                SettingsScreenContent(
                    state =
                        SettingsState(
                            themeConfig = ThemeConfig.SYSTEM,
                            themeStyle = ThemeStyle.DYNAMIC,
                            customThemeColor = null,
                            weightUnit = WeightUnit.KG,
                            heightUnit = HeightUnit.CM,
                            distanceUnit = DistanceUnit.KM,
                            backupStatus = BackupStatus.Idle,
                            updateStatus = UpdateStatus.Idle,
                            downloadStatus = DownloadStatus.Idle,
                        ),
                    appVersion = "1.0.0-dev",
                    isDevMode = true,
                )
            }
        }

        composeRule.onRoot().captureRoboImage()
    }
}

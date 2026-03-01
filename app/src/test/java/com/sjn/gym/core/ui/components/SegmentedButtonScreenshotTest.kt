package com.sjn.gym.core.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
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
class SegmentedButtonScreenshotTest {
    @get:Rule(order = 1)
    val composeRule =
        androidx.compose.ui.test.junit4
            .createComposeRule()

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
    fun testSegmentedButton_FirstSelected() {
        composeRule.setContent {
            GymExeTheme {
                SegmentedButton(
                    options = listOf("Option 1", "Option 2", "Option 3"),
                    selectedOption = "Option 1",
                    onOptionSelected = {},
                )
            }
        }
        composeRule.onRoot().captureRoboImage()
    }
}

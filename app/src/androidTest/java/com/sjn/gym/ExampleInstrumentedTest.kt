package com.sjn.gym

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunchesSuccessfully() {
        // This test simply verifies that MainActivity launches without crashing.
        // If the app crashes on startup (e.g. R8 issues), this test will fail.
        composeTestRule.waitForIdle()
    }
}

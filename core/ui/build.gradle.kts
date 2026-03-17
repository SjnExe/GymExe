plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    api("androidx.compose.foundation:foundation-layout:1.10.5")
    api("androidx.compose.runtime:runtime:1.10.5")
    implementation("androidx.compose.ui:ui-text:1.10.5")
    implementation("androidx.compose.ui:ui-unit:1.10.5")
    implementation("androidx.core:core:1.18.0")

    api(project(":core:model"))

    debugRuntimeOnly(libs.androidx.ui.test.manifest)

    testRuntimeOnly("io.mockk:mockk-agent-android:1.14.9")
    testRuntimeOnly("io.mockk:mockk:1.14.9")
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.vintage.engine)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
}

dependencyAnalysis {
    issues {
        onIncorrectConfiguration {
            exclude("androidx.compose.ui:ui-test-manifest")
        }
    }
}

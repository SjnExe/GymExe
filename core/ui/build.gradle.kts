plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    api(libs.androidx.ui)
    api(project(":core:model"))
    implementation(libs.androidx.core)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.robolectric.annotations)
    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.core)
    testImplementation(libs.roborazzi.junit.rule)
    testImplementation(libs.robolectric)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
}

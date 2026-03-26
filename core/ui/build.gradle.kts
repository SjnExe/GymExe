plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk.agent.android)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.core)
    testImplementation(libs.roborazzi.junit.rule)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testImplementation(libs.robolectric.annotations)
    testImplementation(libs.androidx.ui)
    testImplementation(libs.androidx.compose.ui.test)
    api(libs.androidx.ui)
    api(project(":core:model"))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
}

plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    api(project(":core:model"))
    api(libs.androidx.ui)

    implementation(libs.androidx.core)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.robolectric.annotations)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.core)
    testImplementation(libs.roborazzi.junit.rule)

    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
}

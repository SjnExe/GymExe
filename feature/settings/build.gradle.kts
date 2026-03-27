plugins { id("gymexe.android.feature") }

android {
    namespace = "com.sjn.gym.feature.settings"
    buildFeatures { buildConfig = true }
    defaultConfig {
        val nameProp = project.providers.gradleProperty("versionName").orNull
        buildConfigField("String", "VERSION_NAME", "\"${nameProp ?: "0.0.1"}\"")
    }
}

configurations.maybeCreate("devDebugImplementation")

configurations.maybeCreate("stableDebugImplementation")

configurations.maybeCreate("devReleaseImplementation")

configurations.maybeCreate("stableReleaseImplementation")

configurations.maybeCreate("devBenchmarkImplementation")

configurations.maybeCreate("stableBenchmarkImplementation")

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Kermit is used in main, so it must be available to all variants
    implementation(libs.kermit)
    releaseImplementation(libs.kermit.core)
    "devDebugImplementation"(libs.kermit.core.android.debug)
    "stableDebugImplementation"(libs.kermit.core.android.debug)
    "devDebugImplementation"(libs.kermit.android.debug)
    "stableDebugImplementation"(libs.kermit.android.debug)

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

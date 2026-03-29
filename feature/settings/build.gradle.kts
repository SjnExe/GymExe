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
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.kermit)
    implementation(libs.kotlinx.collections.immutable)

    releaseImplementation(libs.kermit.core)

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

    "devDebugImplementation"(libs.kermit.android.debug)
    "devDebugImplementation"(libs.kermit.core.android.debug)

    ksp(libs.hilt.compiler)

    "stableDebugImplementation"(libs.kermit.android.debug)
    "stableDebugImplementation"(libs.kermit.core.android.debug)
}

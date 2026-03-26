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

configurations.maybeCreate("devReleaseImplementation")

dependencies {
    implementation(libs.androidx.activity.compose)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.core)
    testImplementation(libs.roborazzi.junit.rule)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.robolectric.annotations)
    testImplementation(libs.androidx.compose.ui.test)
    releaseImplementation(libs.kermit.core)
    debugImplementation(libs.kermit.core.android.debug)
    debugImplementation(libs.kermit.android.debug)
    api(project(":core:data"))
    api(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.core)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)

    implementation(libs.bundles.compose.ui)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Kermit is used in main, so it must be available to all variants
    implementation(libs.kermit)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
}

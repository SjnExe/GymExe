plugins {
    id("gymexe.android.application")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
    id("gymexe.roborazzi")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sjn.gym"
    buildFeatures { buildConfig = true }
}

configurations.maybeCreate("devDebugImplementation")

configurations.maybeCreate("devReleaseImplementation")

dependencies {
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk.agent.android)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    releaseImplementation(libs.kermit.core)
    debugImplementation(libs.kermit.core.android.debug)
    debugImplementation(libs.kermit.android.debug)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.common)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.core)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity)
    implementation(libs.kotlin.stdlib)
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:profile"))

    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Kermit used directly in main source set
    implementation(libs.kermit)

    // Properly split Chucker variants
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    runtimeOnly(libs.androidx.profileinstaller)
    debugRuntimeOnly(libs.leakcanary.android)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)
}

plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.onboarding" }

dependencies {
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)

    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)

    ksp(libs.hilt.compiler)
}

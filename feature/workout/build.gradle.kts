plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.workout" }

dependencies {
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.hilt.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
}

plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.workout" }

dependencies {
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)

    ksp(libs.hilt.compiler)
}

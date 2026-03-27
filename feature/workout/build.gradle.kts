plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.workout" }

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    api(project(":core:data"))
    api(project(":core:model"))
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.bundles.compose.ui)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.hilt.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    debugRuntimeOnly(libs.androidx.ui.test.manifest)
    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.robolectric)
}

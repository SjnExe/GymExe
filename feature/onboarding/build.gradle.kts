plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.onboarding" }

dependencies {
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk.agent.android)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    api(project(":core:data"))
    api(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.bundles.compose.ui)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    debugRuntimeOnly(libs.androidx.ui.test.manifest)
    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.robolectric)
}

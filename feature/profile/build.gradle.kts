plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.profile" }

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(project(":core:ui"))
    implementation(libs.androidx.activity)
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(libs.androidx.compose.ui.geometry)
    implementation(libs.hilt.core)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
}

plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
}

android {
    namespace = "com.sjn.gym.feature.onboarding"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    implementation(libs.androidx.core.ktx)
    // Compose BOM, preview, tooling handled by plugin

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // Hilt handled by plugin

    debugImplementation(libs.androidx.ui.test.manifest)
}

plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    implementation(libs.androidx.core.ktx)
    // Compose BOM and tooling added by plugin

    implementation(project(":core:model"))
    // implementation(libs.androidx.ui.tooling.preview) // added by plugin
    // debugImplementation(libs.androidx.ui.tooling) // added by plugin

    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.14.1")
}

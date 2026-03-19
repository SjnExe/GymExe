plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    api(project(":core:model"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
}

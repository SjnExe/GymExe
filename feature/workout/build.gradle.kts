plugins {
    id("gymexe.android.feature")
}

android {
    namespace = "com.sjn.gym.feature.workout"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material.icons.extended)
}

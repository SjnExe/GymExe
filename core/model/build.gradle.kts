plugins {
    id("gymexe.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sjn.gym.core.model"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
}

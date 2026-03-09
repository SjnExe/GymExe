plugins {
    id("gymexe.android.library")
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dependency.analysis)
}

android { namespace = "com.sjn.gym.core.model" }

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
}

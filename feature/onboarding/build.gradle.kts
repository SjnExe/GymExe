plugins {
    id("gymexe.android.feature")
    alias(libs.plugins.dependency.analysis)
}

android { namespace = "com.sjn.gym.feature.onboarding" }

dependencies { implementation(libs.androidx.core.ktx) }

plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.onboarding" }

dependencies {
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)
}

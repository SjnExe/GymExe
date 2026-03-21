plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.onboarding" }

dependencies {
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.workout" }

dependencies {
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.hilt.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

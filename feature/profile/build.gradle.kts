plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.profile" }

dependencies {
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.androidx.compose.ui.geometry)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}

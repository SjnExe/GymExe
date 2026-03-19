plugins { id("gymexe.android.feature") }

android {
    namespace = "com.sjn.gym.feature.settings"
    buildFeatures { buildConfig = true }
    defaultConfig {
        val nameProp = project.providers.gradleProperty("versionName").orNull
        buildConfigField("String", "VERSION_NAME", "\"${nameProp ?: "0.0.1"}\"")
    }
}

dependencies {
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.core)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.hilt.core)

    implementation(libs.kermit)

    "testDevImplementation"(libs.androidx.compose.runtime)
    "testDevImplementation"(libs.androidx.compose.ui.test)
    "testDevImplementation"(libs.androidx.ui.test.junit4)
    "testDevImplementation"(libs.roborazzi.core)
    "testDevImplementation"(libs.robolectric.annotations)
}

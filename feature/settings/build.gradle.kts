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
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.core)

    implementation(libs.kermit)

    "testImplementation"(libs.androidx.compose.runtime)
    "testImplementation"(libs.androidx.compose.ui.test)
    "testImplementation"(libs.androidx.ui.test.junit4)
    "testImplementation"(libs.roborazzi.core)
    "testImplementation"(libs.robolectric.annotations)

    "debugImplementation"(libs.kermit.android.debug)
    "debugImplementation"(libs.kermit.core.android.debug)
    "releaseImplementation"(libs.kermit.core)
}

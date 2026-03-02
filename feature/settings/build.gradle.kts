plugins {
    id("gymexe.android.feature")
    id("gymexe.roborazzi")
}

android {
    namespace = "com.sjn.gym.feature.settings"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        val nameProp = project.findProperty("versionName") as? String
        buildConfigField("String", "VERSION_NAME", "\"${nameProp ?: "0.0.1"}\"")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kermit)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)
}

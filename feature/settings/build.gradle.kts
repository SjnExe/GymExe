plugins {
    id("gymexe.android.feature")
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
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.timber)
}

plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.sjn.gym.feature.settings"

    defaultConfig {
        val nameProp = project.findProperty("versionName") as? String
        buildConfigField("String", "VERSION_NAME", "\"${nameProp ?: "0.0.1"}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.timber)

    debugImplementation(libs.androidx.ui.test.manifest)
}

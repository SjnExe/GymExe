plugins {
    id("gymexe.android.application")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.sjn.gym"

    defaultConfig {
        applicationId = "com.sjn.gym"
        // minSdk, targetSdk handled by convention plugin

        val codeProp = project.findProperty("versionCode") as? String
        val nameProp = project.findProperty("versionName") as? String

        versionCode = codeProp?.toIntOrNull() ?: 1
        versionName = nameProp ?: "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            if (!project.hasProperty("versionName")) {
                versionNameSuffix = "-dev"
            }
        }
        create("stable") {
            dimension = "env"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes.getByName("release").signingConfig = signingConfigs.getByName("release")

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    // Compose BOM, tooling, preview handled by convention plugin

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // Hilt handled by convention plugin

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)

    // Modular dependencies
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:profile"))
}

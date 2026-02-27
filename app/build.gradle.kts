plugins {
    id("gymexe.android.application")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }
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

    // Configure APK Splits for Architecture-specific builds
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
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
        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks += listOf("release")
            // Apply test-specific rules ONLY to this build type
            proguardFile("proguard-test-rules.pro")
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
    buildTypes.getByName("benchmark").signingConfig = signingConfigs.getByName("release")

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Rename APKs using Legacy API (applicationVariants) via explicit cast
// This allows access to the legacy API even if AppExtension isn't the primary extension type
val androidExtension = extensions.getByName("android")
if (androidExtension is com.android.build.gradle.AppExtension) {
    androidExtension.applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val flavorName = variant.flavorName
                val buildType = variant.buildType.name

                val abiName = output.getFilter(com.android.build.OutputFile.ABI)

                val newName = if (flavorName == "stable") {
                    // Stable Format: GymExe-{architecture}.apk
                    // Example: GymExe-arm64-v8a.apk, GymExe-universal.apk
                    if (abiName != null) {
                        "GymExe-$abiName.apk"
                    } else {
                        "GymExe-universal.apk"
                    }
                } else {
                    // Dev/Default Format: GymExe-{flavor}-{buildType}.apk
                    // We only care about Universal for Dev usually, but splits are enabled globally.
                    // If universal, we name it GymExe-dev-release.apk
                    // If split, we append ABI: GymExe-dev-release-arm64-v8a.apk
                    val base = if (flavorName.isNullOrEmpty()) "GymExe-$buildType" else "GymExe-$flavorName-$buildType"
                    if (abiName != null) {
                        "$base-$abiName.apk"
                    } else {
                        "$base.apk"
                    }
                }
                output.outputFileName = newName
            }
    }
}

// Enable AndroidTest ONLY for devBenchmark to allow testing R8 builds
// We disable it for standard Release to save build time and keep it pure.
androidComponents {
    beforeVariants(selector().withBuildType("benchmark")) { variantBuilder ->
        (variantBuilder as? com.android.build.api.variant.HasAndroidTestBuilder)?.enableAndroidTest = true
    }
    beforeVariants(selector().withBuildType("release")) { variantBuilder ->
        (variantBuilder as? com.android.build.api.variant.HasAndroidTestBuilder)?.enableAndroidTest = false
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
    implementation(libs.androidx.tracing)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // Hilt handled by convention plugin

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.tracing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)
    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)
    implementation(libs.timber)

    // Modular dependencies
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:profile"))
}

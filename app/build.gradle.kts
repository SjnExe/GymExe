import com.android.build.api.variant.FilterConfiguration

plugins {
    id("gymexe.android.application")
    id("gymexe.android.compose")
    id("gymexe.android.hilt")
    id("gymexe.roborazzi")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dependencyGuard)
}

dependencyGuard { configuration("devReleaseRuntimeClasspath") }

android {
    buildFeatures { buildConfig = true }
    namespace = "com.sjn.gym"

    defaultConfig {
        applicationId = "com.sjn.gym"
        // minSdk, targetSdk handled by convention plugin

        // Base defaults, dynamic versions injected at execution time via onVariants
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    // Configure APK Splits for Architecture-specific builds
    splits {
        abi {
            isEnable = true
            reset()
            
            if (project.hasProperty("emulatorOnly")) {
                // CI Emulator tests: Only build x86 and x86_64. Skip ARM and Universal.
                include("x86", "x86_64")
                isUniversalApk = false
            } else {
                // Local or Production release: Build everything
                include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
                isUniversalApk = true
            }
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
        debug { applicationIdSuffix = ".debug" }
        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks += listOf("release")
            // Apply test-specific rules ONLY to this build type
            proguardFile("proguard-test-rules.pro")
            // Also apply to the androidTest APK so the test runner and references survive
            testProguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-test-rules.pro",
            )
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
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
        jniLibs {
            // Keep debug symbols for libraries that fail to strip, preventing warnings
            keepDebugSymbols += "**/libandroidx.graphics.path.so"
            keepDebugSymbols += "**/libdatastore_shared_counter.so"
        }
    }
}

// Enable AndroidTest ONLY for devBenchmark to allow testing R8 builds
// We disable it for standard Release to save build time and keep it pure.
androidComponents {
    beforeVariants(selector().withBuildType("benchmark")) { variantBuilder ->
        (variantBuilder as? com.android.build.api.variant.HasAndroidTestBuilder)
            ?.enableAndroidTest = true
    }
    beforeVariants(selector().withBuildType("release")) { variantBuilder ->
        (variantBuilder as? com.android.build.api.variant.HasAndroidTestBuilder)
            ?.enableAndroidTest = false
    }

    onVariants { variant ->
        val codeProp = project.providers.gradleProperty("versionCode")
        val nameProp = project.providers.gradleProperty("versionName")

        variant.outputs.forEach { output ->
            // Dynamically inject versionCode and versionName at execution time to preserve
            // Configuration Cache
            output.versionCode.set(codeProp.map { it.toIntOrNull() ?: 1 }.orElse(1))
            output.versionName.set(
                nameProp.orElse(if (variant.flavorName == "dev") "0.0.1-dev" else "0.0.1")
            )

            val buildType = variant.buildType
            val flavorName = variant.flavorName

            val abiName =
                output.filters
                    .find { it.filterType == FilterConfiguration.FilterType.ABI }
                    ?.identifier

            val newName =
                if (flavorName == "stable") {
                    // Stable Format: GymExe-{architecture}.apk
                    // Example: GymExe-arm64-v8a.apk, GymExe-universal.apk
                    if (abiName != null) {
                        "GymExe-$abiName.apk"
                    } else {
                        "GymExe-universal.apk"
                    }
                } else {
                    // Dev/Default Format: GymExe-{flavor}-{buildType}-{architecture}.apk
                    // Example: GymExe-dev-release-universal.apk, GymExe-dev-debug-arm64-v8a.apk
                    val base =
                        if (flavorName.isNullOrEmpty()) "GymExe-$buildType"
                        else "GymExe-$flavorName-$buildType"
                    if (abiName != null) {
                        "$base-$abiName.apk"
                    } else {
                        "$base-universal.apk"
                    }
                }

            // Use reflection to set outputFileName as it is not exposed in the VariantOutput
            // interface
            // but is available on the implementation (VariantOutputImpl).
            try {
                val outputFileNameMethod = output::class.java.getMethod("getOutputFileName")

                @Suppress("UNCHECKED_CAST")
                val outputFileNameProperty =
                    outputFileNameMethod.invoke(output) as? org.gradle.api.provider.Property<String>
                outputFileNameProperty?.set(newName)
            } catch (e: Exception) {
                println("Failed to rename APK: ${e.message}")
            }
        }
    }
}

dependencies {
    implementation(libs.kermit)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.common)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime)

    implementation(libs.dagger)
    implementation(libs.hilt.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.okhttp)

    "androidTestImplementation"(libs.junit)

    "testImplementation"(libs.androidx.compose.runtime)
    "testImplementation"(libs.androidx.compose.ui.test)
    "testImplementation"(libs.roborazzi.core)
    "testImplementation"(libs.robolectric.annotations)

    "androidTestImplementation"(libs.androidx.junit)
    "androidTestImplementation"(platform(libs.androidx.compose.bom))
    "androidTestImplementation"(libs.androidx.ui.test.junit4)
    "testImplementation"(libs.androidx.ui.test.junit4)

    debugImplementation(libs.bundles.compose.debug)
    debugImplementation(libs.leakcanary.android)

    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)

    // Modular dependencies
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:profile"))
}

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
    namespace = "com.sjn.gym"
    buildFeatures { buildConfig = true }

    defaultConfig {
        applicationId = "com.sjn.gym"
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

configurations.maybeCreate("devDebugImplementation")

configurations.maybeCreate("devReleaseImplementation")

configurations.maybeCreate("devDebugAndroidTestRuntimeOnly")

dependencies {
    implementation(libs.dagger)
    implementation(libs.javax.inject)
    implementation(libs.androidx.activity.compose)
    releaseImplementation(libs.kermit.core)
    debugImplementation(libs.kermit.core.android.debug)
    debugImplementation(libs.kermit.android.debug)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.common)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.core)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity)
    implementation(libs.kotlin.stdlib)
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:workout"))
    implementation(project(":feature:profile"))

    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.compose.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Kermit used directly in main source set
    implementation(libs.kermit)

    // Properly split Chucker variants
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
    "benchmarkImplementation"(libs.chucker.release)

    runtimeOnly(libs.androidx.profileinstaller)
    debugRuntimeOnly(libs.leakcanary.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test:monitor:1.8.0")
    androidTestRuntimeOnly("androidx.test:runner:1.6.1")
    androidTestRuntimeOnly(libs.androidx.test.core)
    androidTestImplementation(libs.junit)
    "devDebugAndroidTestRuntimeOnly"(libs.kotlin.stdlib)
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
}

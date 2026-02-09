plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

fun getCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .redirectErrorStream(true)
            .start()
        process.inputStream.bufferedReader().readText().trim().toInt()
    } catch (e: Exception) {
        1 // Fallback
    }
}

fun getVersionName(): String {
    val envVersion = System.getenv("VERSION_NAME")
    return if (!envVersion.isNullOrEmpty()) {
        envVersion
    } else {
        "1.0-dev"
    }
}

android {
    namespace = "com.sjn.gymexe"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sjn.gymexe"
        minSdk = 26
        targetSdk = 36
        versionCode = getCommitCount()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add Build Time for Update Logic
        buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
    }

    signingConfigs {
        create("release") {
            storeFile = file("${project.rootDir}/Dev/release.keystore")
            storePassword = "gymexe"
            keyAlias = "gymexe"
            keyPassword = "gymexe"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Temporarily disabled to resolve R8 crash in Beta
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".beta"
        }
        create("prod") {
            dimension = "env"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a", "x86_64")
            isUniversalApk = true
        }
    }

    lint {
        // Suppress ObsoleteLintCustomCheck because an external library's lint check is crashing
        // and we cannot update it further without breaking the build (e.g. Hilt requires AGP 9.0).
        disable += "ObsoleteLintCustomCheck"
        // Suppress LintError to prevent the build from failing when a lint detector crashes
        disable += "LintError"
        // Suppress NullSafeMutableLiveData due to crash in androidx.lifecycle lint check with Kotlin 2.1
        disable += "NullSafeMutableLiveData"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Add Extended Icons (includes Core)
    implementation(libs.androidx.compose.material.icons.extended)
    // Window Size Class for Adaptive UI
    implementation(libs.androidx.material3.window.size)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

group = "com.sjn.gym.buildlogic"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "25"
    targetCompatibility = "25"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kover.gradlePlugin)
    implementation(libs.roborazzi.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gymexe.android.application"
            implementationClass = "com.sjn.gym.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "gymexe.android.library"
            implementationClass = "com.sjn.gym.convention.AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = "gymexe.jvm.library"
            implementationClass = "com.sjn.gym.convention.JvmLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "gymexe.android.hilt"
            implementationClass = "com.sjn.gym.convention.AndroidHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "gymexe.android.compose"
            implementationClass = "com.sjn.gym.convention.AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "gymexe.android.feature"
            implementationClass = "com.sjn.gym.convention.AndroidFeatureConventionPlugin"
        }
        register("androidRoom") {
            id = "gymexe.android.room"
            implementationClass = "com.sjn.gym.convention.AndroidRoomConventionPlugin"
        }
        register("androidTest") {
            id = "gymexe.android.test"
            implementationClass = "com.sjn.gym.convention.AndroidTestConventionPlugin"
        }
        register("spotless") {
            id = "gymexe.spotless"
            implementationClass = "com.sjn.gym.convention.SpotlessConventionPlugin"
        }
        register("roborazzi") {
            id = "gymexe.roborazzi"
            implementationClass = "com.sjn.gym.convention.RoborazziConventionPlugin"
        }
        register("kover") {
            id = "gymexe.kover"
            implementationClass = "com.sjn.gym.convention.KoverConventionPlugin"
        }
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }
}

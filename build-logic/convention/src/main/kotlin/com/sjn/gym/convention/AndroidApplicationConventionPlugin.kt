package com.sjn.gym.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("gymexe.spotless")
                apply("gymexe.kover")
                apply("com.autonomousapps.dependency-analysis")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = 36
                defaultConfig.minSdk = 26
                compileSdk = 36

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                testOptions {
                    unitTests.isIncludeAndroidResources = true
                    unitTests.all {
                        val testTask = this as? org.gradle.api.tasks.testing.Test
                        testTask?.useJUnitPlatform()
                        testTask?.setProperty("failOnNoDiscoveredTests", false)
                        testTask?.jvmArgs("--enable-native-access=ALL-UNNAMED")
                    }
                }

                // Common configuration function
                configureKotlinAndroid(this)

                flavorDimensions += "env"
                productFlavors {
                    create("dev") {
                        dimension = "env"
                        applicationIdSuffix = ".dev"
                        if (!project.providers.gradleProperty("versionName").isPresent) {
                            versionNameSuffix = "-dev"
                        }
                    }
                    create("stable") {
                        dimension = "env"
                    }
                }
            }
        }
    }
}

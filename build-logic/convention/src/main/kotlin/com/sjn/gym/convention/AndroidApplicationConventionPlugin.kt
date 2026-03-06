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
                apply("org.jetbrains.kotlin.android")
                apply("gymexe.spotless")
                apply("gymexe.detekt")
                apply("gymexe.kover")
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
                    }
                }

                // Common configuration function
                configureKotlinAndroid(this)

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
            }
        }
    }
}

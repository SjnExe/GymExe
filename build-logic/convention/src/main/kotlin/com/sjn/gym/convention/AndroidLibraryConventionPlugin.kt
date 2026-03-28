package com.sjn.gym.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("gymexe.spotless")
                apply("gymexe.kover")
                apply("gymexe.android.test")
                apply("gymexe.dependency.analysis")
                apply("com.squareup.sort-dependencies")
            }

            extensions.configure<com.autonomousapps.DependencyAnalysisSubExtension> {
                issues {
                    onIncorrectConfiguration {
                        exclude("com.google.dagger:hilt-android")
                    }
                }
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.minSdk = 26
                compileSdk = 36

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.consumerProguardFiles("consumer-rules.pro")

                buildFeatures {
                    buildConfig = false
                    resValues = false
                }

                testFixtures {
                    enable = true
                }

                testOptions {
                    unitTests.isIncludeAndroidResources = true
                    unitTests.all { test ->
                        val testTask = test as? org.gradle.api.tasks.testing.Test
                        testTask?.useJUnitPlatform()
                        testTask?.setProperty("failOnNoDiscoveredTests", false)
                        testTask?.jvmArgs("--enable-native-access=ALL-UNNAMED")
                        testTask?.systemProperty("junit.jupiter.execution.failIfNoTests", "false")
                    }
                }

                lint {
                    lintConfig = file("${target.rootDir}/gradle/lint.xml")
                    abortOnError = true
                    checkDependencies = true
                    warningsAsErrors = true
                }

                configureKotlinAndroid(this)

                flavorDimensions += "env"
                productFlavors {
                    create("dev") {
                        dimension = "env"
                    }
                    create("stable") {
                        dimension = "env"
                    }
                }
            }
        }
    }
}

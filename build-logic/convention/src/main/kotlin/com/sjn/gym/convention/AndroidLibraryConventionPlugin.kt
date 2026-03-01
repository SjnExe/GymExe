package com.sjn.gym.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.api.tasks.testing.Test

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("gymexe.spotless")
                apply("gymexe.detekt")
            }

            val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("junit.jupiter").get())
                add("testRuntimeOnly", libs.findLibrary("junit.jupiter.engine").get())
                add("testRuntimeOnly", libs.findLibrary("junit.platform.launcher").get())
                add("testRuntimeOnly", libs.findLibrary("junit.vintage.engine").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("mockk.agent").get())
                add("testImplementation", libs.findLibrary("mockk.android").get())
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.minSdk = 26
                compileSdk = 36

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                defaultConfig.consumerProguardFiles("consumer-rules.pro")

                testOptions {
                    unitTests.isIncludeAndroidResources = true
                    unitTests.all { test ->
                        test.useJUnitPlatform()
                    }
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

            // In Gradle 8+, the Test task has a Property<Boolean> called failOnNoDiscoveredTests
            // To be version agnostic and avoid compilation errors if not present, we can just:
            tasks.withType(Test::class.java).configureEach {
                if (hasProperty("failOnNoDiscoveredTests")) {
                    try {
                        val property = property("failOnNoDiscoveredTests") as org.gradle.api.provider.Property<Boolean>
                        property.set(false)
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}

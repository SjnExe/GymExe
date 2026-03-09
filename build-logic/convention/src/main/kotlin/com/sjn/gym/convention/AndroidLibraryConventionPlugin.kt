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
            }

            val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("junit.jupiter").get())
                add("testRuntimeOnly", libs.findLibrary("junit.jupiter.engine").get())
                add("testRuntimeOnly", libs.findLibrary("junit.platform.launcher").get())
                add("testRuntimeOnly", libs.findLibrary("junit.vintage.engine").get())
                add("testImplementation", "org.junit.vintage:junit-vintage-engine")
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
                        val testTask = test as? org.gradle.api.tasks.testing.Test
                        testTask?.useJUnitPlatform()
                        testTask?.setProperty("failOnNoDiscoveredTests", false)
                        testTask?.systemProperty("junit.jupiter.execution.failIfNoTests", "false")
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
        }
    }
}

package com.sjn.gym.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("kotlin-compose").get().get().pluginId)

            pluginManager.withPlugin(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("android-application").get().get().pluginId) {
                extensions.configure<ApplicationExtension> {
                    buildFeatures { compose = true }
                }
            }

            pluginManager.withPlugin(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("android-library").get().get().pluginId) {
                extensions.configure<LibraryExtension> {
                    buildFeatures { compose = true }
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("api", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("api", libs.findBundle("compose-core").get())
                add("debugRuntimeOnly", libs.findLibrary("androidx-ui-test-manifest").get())
                add("lintChecks", libs.findLibrary("slack-compose-lints").get())
            }
        }
    }
}

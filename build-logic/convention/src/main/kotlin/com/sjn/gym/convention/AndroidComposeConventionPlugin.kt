package com.sjn.gym.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.findByType(LibraryExtension::class.java)
                ?: extensions.findByType(ApplicationExtension::class.java)
                ?: return

            extension.apply {
                buildFeatures {
                    compose = true
                }
            }

            val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
                add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
            }
        }
    }
}

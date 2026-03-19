package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("gymexe.android.library")
            pluginManager.apply("gymexe.android.hilt")
            pluginManager.apply("gymexe.android.compose")
            pluginManager.apply("gymexe.roborazzi")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("api", project(":core:model"))
                add("api", project(":core:data"))

                add("api", libs.findLibrary("androidx-lifecycle-viewmodel").get())
                add("implementation", libs.findLibrary("hilt-lifecycle-viewmodel-compose").get())

                add("api", libs.findLibrary("kotlinx-coroutines-core").get())

                add("implementation", libs.findLibrary("androidx-activity").get())
                add("implementation", libs.findLibrary("androidx-compose-material-icons-core").get())

                add("androidTestRuntimeOnly", libs.findLibrary("androidx-test-core").get())
            }
        }
    }
}

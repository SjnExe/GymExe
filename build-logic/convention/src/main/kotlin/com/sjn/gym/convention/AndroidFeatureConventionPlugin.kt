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
                                
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel").get())
                add("implementation", libs.findLibrary("hilt-lifecycle-viewmodel-compose").get())

                add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())

                add("implementation", libs.findLibrary("androidx-compose-material-icons-core").get())
                add("implementation", libs.findLibrary("androidx-material-icons-extended").get())

                add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("implementation", libs.findBundle("compose-core").get())
                add("implementation", libs.findLibrary("androidx-ui").get())
                add("implementation", libs.findLibrary("androidx-ui-graphics").get())
                add("implementation", libs.findLibrary("androidx-material3").get())

                add("androidTestRuntimeOnly", libs.findLibrary("androidx-test-core").get())

                add("lintChecks", libs.findLibrary("slack-compose-lints").get())
            }
        }
    }
}

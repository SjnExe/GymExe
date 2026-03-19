package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")
            configurations.maybeCreate("testDevImplementation")
            configurations.maybeCreate("testDevRuntimeOnly")
            dependencies {
                add("implementation", libs.findBundle("room").get())
                add("ksp", libs.findLibrary("androidx.room.compiler").get())
            }

        }
    }
}

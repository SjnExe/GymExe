package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("hilt").get().get().pluginId)
                apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("ksp").get().get().pluginId)
            }

            val libs = extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs")
            dependencies {
                add("api", libs.findLibrary("dagger").get())
                add("api", libs.findLibrary("javax-inject").get())
                add("implementation", libs.findLibrary("hilt-core").get())
                add("implementation", libs.findLibrary("hilt-android").get())
                add("ksp", libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}

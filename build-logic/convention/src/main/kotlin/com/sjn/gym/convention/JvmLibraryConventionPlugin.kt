package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("gymexe.spotless")
                apply("gymexe.kover")
                apply("gymexe.dependency.analysis")
                apply("com.squareup.sort-dependencies")
            }
            configureKotlinJvm()
        }
    }
}

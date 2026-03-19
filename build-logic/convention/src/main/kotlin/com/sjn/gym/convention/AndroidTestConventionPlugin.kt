package com.sjn.gym.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

            configurations.maybeCreate("testDevImplementation")
            configurations.maybeCreate("testDevRuntimeOnly")
            dependencies {
                add("testDevImplementation", libs.findBundle("testing-common").get())
                add("testDevImplementation", libs.findLibrary("junit-jupiter").get())
                add("testDevRuntimeOnly", libs.findLibrary("junit-jupiter-engine").get())
                add("testDevRuntimeOnly", libs.findLibrary("junit-platform-launcher").get())
                add("testDevRuntimeOnly", libs.findLibrary("junit-vintage-engine").get())
                add("testDevImplementation", libs.findLibrary("turbine").get())
                add("testDevImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}

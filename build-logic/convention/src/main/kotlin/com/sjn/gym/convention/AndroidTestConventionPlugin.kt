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

            configurations.maybeCreate("testImplementation")
            configurations.maybeCreate("testRuntimeOnly")
            dependencies {
                add("testImplementation", libs.findBundle("testing-common").get())
                add("testImplementation", libs.findLibrary("junit-jupiter").get())
                add("testRuntimeOnly", libs.findLibrary("junit-jupiter-engine").get())
                add("testRuntimeOnly", libs.findLibrary("junit-platform-launcher").get())
                add("testRuntimeOnly", libs.findLibrary("junit-vintage-engine").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}

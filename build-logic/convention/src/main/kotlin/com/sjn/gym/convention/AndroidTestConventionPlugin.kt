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
        }
    }
}

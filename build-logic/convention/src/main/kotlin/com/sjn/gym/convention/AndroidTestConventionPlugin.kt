package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs")
            
            configurations.maybeCreate("testImplementation")
            configurations.maybeCreate("testRuntimeOnly")

            dependencies {
            }
        }
    }
}

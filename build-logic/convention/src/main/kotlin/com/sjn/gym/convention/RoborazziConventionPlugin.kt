package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class RoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("roborazzi").get().get().pluginId)

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            configurations.maybeCreate("testImplementation")
            configurations.maybeCreate("testRuntimeOnly")
            dependencies {

            }

            tasks.withType(org.gradle.api.tasks.testing.Test::class.java).configureEach {
                systemProperty("robolectric.graphicsMode", "NATIVE")
                systemProperty("robolectric.pixelCopyRenderMode", "hardware")
                
                // Add this line to silence the Conscrypt native access warning
                jvmArgs("--enable-native-access=ALL-UNNAMED")
            }
        }
    }
}

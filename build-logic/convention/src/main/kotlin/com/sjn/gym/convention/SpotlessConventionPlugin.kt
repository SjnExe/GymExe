package com.sjn.gym.convention

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            extensions.configure<SpotlessExtension> {
                yaml {
                    target("**/*.yml", "**/*.yaml")
                    targetExclude("**/build/**/*.yml")
                    jackson().yamlFeature("WRITE_DOC_START_MARKER", true)
                }
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktfmt().kotlinlangStyle()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                kotlinGradle {
                    target("*.gradle.kts")
                    ktfmt().kotlinlangStyle()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }
        }
    }
}

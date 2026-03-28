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
                    jackson()
                        .yamlFeature("SPLIT_LINES", false)
                        .yamlFeature("MINIMIZE_QUOTES", true)
                        .yamlFeature("LITERAL_BLOCK_STYLE", false)
                        .yamlFeature("INDENT_ARRAYS_WITH_INDICATOR", true)
                        .yamlFeature("WRITE_DOC_START_MARKER", false)
                        .yamlFeature("ALWAYS_QUOTE_NUMBERS_AS_STRINGS", true)
                        .yamlFeature("USE_PLATFORM_LINE_BREAKS", false)
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
                java {
                    target("**/*.java")
                    targetExclude("**/build/**/*.java")
                    googleJavaFormat()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("xml") {
                    target("**/*.xml")
                    targetExclude("**/build/**/*.xml")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("toml") {
                    target("**/*.toml")
                    targetExclude("**/build/**/*.toml")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("properties") {
                    target("**/*.properties")
                    targetExclude("**/build/**/*.properties")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("markdown") {
                    target("**/*.md")
                    targetExclude("**/build/**/*.md")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("proguard") {
                    target("**/*.pro")
                    targetExclude("**/build/**/*.pro")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }
        }
    }
}

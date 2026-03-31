package com.sjn.gym.convention

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("spotless").get().get().pluginId)

            extensions.configure<SpotlessExtension> {
                yaml {
                    target("**/*.yml", "**/*.yaml")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
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
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    ktfmt().kotlinlangStyle()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                kotlinGradle {
                    target("*.gradle.kts")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    ktfmt().kotlinlangStyle()
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                java {
                    target("**/*.java")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    licenseHeader("/* This is a generated file, do not edit it. */\n")
                    custom("NoJavaAllowed", object : java.io.Serializable, com.diffplug.spotless.FormatterFunc {
                        override fun apply(input: String): String {
                            throw org.gradle.api.GradleException("Java files are not allowed, use Kotlin instead.")
                        }
                    })
                }
                format("json") {
                    target("**/*.json")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    prettier().config(mapOf("parser" to "json", "tabWidth" to 2))
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("xml") {
                    target("**/*.xml")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("toml") {
                    target("**/*.toml")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("properties") {
                    target("**/*.properties")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("markdown") {
                    target("**/*.md")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                format("proguard") {
                    target("**/*.pro")
                    targetExclude("**/.gradle/**", "**/.kotlin/**", "**/build/**", "**/generated/**", "**/bin/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }
        }
    }
}

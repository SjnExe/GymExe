package com.sjn.gym.convention

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                toolVersion = "1.23.8"
                source.setFrom(files("src/main/java", "src/main/kotlin"))
                config.setFrom(files("$rootDir/detekt.yml"))
                buildUponDefaultConfig = true
                parallel = true
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget = "21"
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(true)
                }
            }
        }
    }
}

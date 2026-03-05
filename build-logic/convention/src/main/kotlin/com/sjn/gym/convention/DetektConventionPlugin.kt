package com.sjn.gym.convention

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.detekt")

            extensions.configure<DetektExtension> {
                source.setFrom(files("src/main/java", "src/main/kotlin"))
                config.setFrom(files("$rootDir/detekt.yml"))
                buildUponDefaultConfig.set(true)
                parallel.set(true)
            }

            tasks.withType<Detekt>().configureEach {
                jdkHome.set(file(System.getProperty("java.home")))
                reports {
                    html.required.set(true)
                    checkstyle.required.set(true)
                }
            }
        }
    }
}

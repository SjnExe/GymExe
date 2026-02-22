package com.sjn.gym.convention

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.api.JavaVersion
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (JavaVersion.current() >= JavaVersion.VERSION_22) { // 22 is where Detekt might start failing or specifically 25
            // Detekt 1.23.x crashes on Java 25 due to IntelliJ Core incompatibility.
            target.logger.warn("Skipping Detekt plugin application because Java version ${JavaVersion.current()} is too new (Detekt 1.23.x supports up to Java 21/22).")
            return
        }

        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                toolVersion = "1.23.8"
                source.setFrom(files("src/main/java", "src/main/kotlin"))
                config.setFrom(files("$rootDir/detekt.yml"))
                buildUponDefaultConfig = true
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget = "21"
            }
        }
    }
}

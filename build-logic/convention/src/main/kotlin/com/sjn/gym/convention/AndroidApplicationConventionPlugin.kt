package com.sjn.gym.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("gymexe.spotless")
                apply("gymexe.detekt")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = 36
                defaultConfig.minSdk = 26
                compileSdk = 36

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                // Common configuration function
                configureKotlinAndroid(this)
            }
        }
    }
}

package com.sjn.gym.convention

import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("kotlin-jvm").get().get().pluginId)
                apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("android-lint").get().get().pluginId)
                apply("gymexe.spotless")
                apply("gymexe.kover")
                apply("gymexe.dependency.analysis")
                apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("sortDependencies").get().get().pluginId)
            }
            
            configureKotlinJvm()

            extensions.configure<Lint> {
                lintConfig = file("${target.rootDir}/gradle/lint.xml")
            }
        }
    }
}

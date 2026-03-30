package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyAnalysisConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("dependencyAnalysis").get().get().pluginId)
        }
    }
}

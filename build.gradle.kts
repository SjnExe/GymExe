plugins {
alias(libs.plugins.android.application) apply false
alias(libs.plugins.android.library) apply false
alias(libs.plugins.kotlin.android) apply false
alias(libs.plugins.kotlin.compose) apply false
alias(libs.plugins.kotlin.serialization) apply false
alias(libs.plugins.ksp) apply false
alias(libs.plugins.hilt) apply false
alias(libs.plugins.spotless) apply false
alias(libs.plugins.versionCatalogUpdate)
alias(libs.plugins.kover)
alias(libs.plugins.dependencyAnalysis)
}

dependencyAnalysis {
val warnOnly = project.hasProperty("warnDependencies")
val printToConsole = project.hasProperty("printDependencies")

    issues {
        all {
            onAny {
                severity("warn")
            }
            
        }
    }

reporting {
printBuildHealth(printToConsole)
}
}

dependencies {
kover(project(":app"))
kover(project(":core:ui"))
kover(project(":core:data"))
kover(project(":core:model"))
kover(project(":feature:settings"))
kover(project(":feature:workout"))
kover(project(":feature:profile"))
kover(project(":feature:onboarding"))
}

tasks.withType<nl.littlerobots.vcu.plugin.VersionCatalogUpdateTask>().configureEach {
notCompatibleWithConfigurationCache("The VersionCatalogUpdateTask is not compatible with the configuration cache")
}
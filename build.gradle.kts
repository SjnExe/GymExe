// Top-level build file where you can add configuration options common to all sub-projects/modules.
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
    alias(libs.plugins.moduleGraph)
}

moduleGraphAssert {
    maxHeight = 4
    allowed = arrayOf(
        ".* -> :core:.*",
        ":feature:.* -> :core:.*",
        ":app -> :feature:.*",
        ":app -> :core:.*"
    )
    restricted = arrayOf(
        ":core:model -X> :core:data", // Model should not depend on data
        ":core:model -X> :core:ui",   // Model should not depend on UI
        ":feature:.* -X> :feature:.*" // Features should not depend on each other
    )
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

tasks.withType<Task>().configureEach {
    if (name.startsWith("assert")) {
        notCompatibleWithConfigurationCache("ModuleGraphAssert is not compatible with the configuration cache")
    }
}

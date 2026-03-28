plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.spotless) apply true
    alias(libs.plugins.versionCatalogUpdate)
    alias(libs.plugins.kover)
    alias(libs.plugins.dependencyAnalysis)
}

dependencyAnalysis {
    val warnOnly = project.hasProperty("warnDependencies")
    val printToConsole = project.hasProperty("printDependencies")

    issues {
        all {
            onAny { severity("warn") }
            onIncorrectConfiguration {
                exclude(
                    "com.google.dagger:dagger",
                    "javax.inject:javax.inject",
                    "com.google.dagger:hilt-android",
                )
            }
        }
    }

    reporting { printBuildHealth(printToConsole) }
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
    notCompatibleWithConfigurationCache(
        "The VersionCatalogUpdateTask is not compatible with the configuration cache"
    )
}

spotless {
    yaml {
        target(".github/**/*.yml", ".github/**/*.yaml", "*.yml", "*.yaml")
        jackson()
            .yamlFeature("SPLIT_LINES", false)
            .yamlFeature("MINIMIZE_QUOTES", true)
            .yamlFeature("LITERAL_BLOCK_STYLE", false)
            .yamlFeature("INDENT_ARRAYS_WITH_INDICATOR", true)
            .yamlFeature("WRITE_DOC_START_MARKER", false)
            .yamlFeature("ALWAYS_QUOTE_NUMBERS_AS_STRINGS", true)
            .yamlFeature("USE_PLATFORM_LINE_BREAKS", false)
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktfmt().kotlinlangStyle()
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("toml") {
        target("*.toml", "gradle/*.toml")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("properties") {
        target("*.properties", "gradle/*.properties")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("markdown") {
        target("*.md")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

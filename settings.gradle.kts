
val requestedTasks = gradle.startParameter.taskNames
if (requestedTasks.any { it.contains("vCU") || it.contains("versionCatalogUpdate") || it.contains("buildHealth") || it.contains("generateBuildHealth") }) {
    gradle.startParameter.systemPropertiesArgs["org.gradle.unsafe.isolated-projects"] = "false"
    // For configuration cache, setting the system property before project evaluation often disables it:
    gradle.startParameter.systemPropertiesArgs["org.gradle.configuration-cache"] = "false"
}

pluginManagement {
    includeBuild("build-logic")
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("com.gradle.develocity") version "4.3.2"
    id("org.gradle.toolchains.foojay-resolver-convention")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GymExe"
include(":app")
include(":core:ui")
include(":core:model")
include(":core:data")
include(":feature:onboarding")
include(":feature:settings")
include(":feature:workout")
include(":feature:profile")

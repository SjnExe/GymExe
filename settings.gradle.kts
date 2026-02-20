pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
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

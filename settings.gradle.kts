pluginManagement {
    includeBuild("build-logic")
    val develocityVersion = file("gradle/libs.versions.toml").readLines()
        .find { it.startsWith("develocity =") }?.substringAfter("\"")?.substringBefore("\"")
    plugins {
        id("com.gradle.develocity") version develocityVersion
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("com.gradle.develocity")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")

        val isCI = System.getenv("CI") == "true"
        val isDevelocityEnabled = providers.gradleProperty("enableDevelocity").getOrElse("false") == "true"

        publishing.onlyIf { isCI || isDevelocityEnabled }
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

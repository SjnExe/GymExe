pluginManagement {
    includeBuild("build-logic")
    val libsToml = file("gradle/libs.versions.toml").readLines()
    val develocityVersion = libsToml.find { it.startsWith("develocity =") }?.substringAfter("\"")?.substringBefore("\"")
    val s3BuildCacheVersion = libsToml.find { it.startsWith("s3BuildCache =") }?.substringAfter("\"")?.substringBefore("\"")
    
    plugins {
        id("com.gradle.develocity") version develocityVersion
        id("com.github.burrunan.s3-build-cache") version s3BuildCacheVersion
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
    id("com.github.burrunan.s3-build-cache") // Added S3 Cache Plugin
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

// Added the Build Cache block for the main app
buildCache {
    local {
        isEnabled = true
    }

    val r2Endpoint = System.getenv("R2_ENDPOINT")

    if (!r2Endpoint.isNullOrBlank()) {
        val r2AccessKey = System.getenv("R2_ACCESS_KEY_ID")
        val r2SecretKey = System.getenv("R2_SECRET_ACCESS_KEY")

        require(!r2AccessKey.isNullOrBlank() && !r2SecretKey.isNullOrBlank()) {
            "R2_ENDPOINT is configured, but R2_ACCESS_KEY_ID or R2_SECRET_ACCESS_KEY is missing."
        }

        remote<com.github.burrunan.s3cache.AwsS3BuildCache> {
            bucket = "gradle-cache"
            endpoint = r2Endpoint
            region = "auto"
            awsAccessKeyId = r2AccessKey
            awsSecretKey = r2SecretKey
            isPush = true
            forcePathStyle = true
        }
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

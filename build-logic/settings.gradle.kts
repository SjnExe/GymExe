pluginManagement {
    val libsToml = file("../gradle/libs.versions.toml").readLines()
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
    id("com.github.burrunan.s3-build-cache")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

buildCache {
    local {
        isEnabled = true
    }

    val r2Endpoint = System.getenv("R2_ENDPOINT")
    val r2AccessKey = System.getenv("R2_ACCESS_KEY_ID")
    val r2SecretKey = System.getenv("R2_SECRET_ACCESS_KEY")

    if (!r2Endpoint.isNullOrBlank()) {
        remote<com.github.burrunan.s3cache.AwsS3BuildCache> {
            bucket = "gradle-cache"
            endpoint = r2Endpoint
            region = "auto"
            awsAccessKeyId = r2AccessKey
            awsSecretKey = r2SecretKey
            isPush = true
        }
    }
}

rootProject.name = "build-logic"
include(":convention")

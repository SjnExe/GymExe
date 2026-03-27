package com.sjn.gym.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import org.gradle.api.artifacts.VersionCatalogsExtension

internal fun Project.configureKotlinJvm() {
    val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
    val javaToolchainVersionString = libs.findVersion("javaToolchainVersion").get().toString()
    val javaTargetVersionString = libs.findVersion("javaTargetVersion").get().toString()
    val javaTargetVersion = JavaVersion.toVersion(javaTargetVersionString)

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = javaTargetVersion
        targetCompatibility = javaTargetVersion
    }

    configureKotlin(javaToolchainVersionString, javaTargetVersionString)
}

internal fun Project.configureKotlin(javaToolchainVersionString: String, javaTargetVersionString: String) {
    // Configure Kotlin toolchain
    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(javaToolchainVersionString.toInt())
    }

    // Ensure JavaCompile tasks use the same toolchain
    val javaToolchains = extensions.getByType(JavaToolchainService::class.java)
    tasks.withType<JavaCompile>().configureEach {
        javaCompiler.set(
            javaToolchains.compilerFor {
                languageVersion.set(JavaLanguageVersion.of(javaToolchainVersionString.toInt()))
            },
        )
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaTargetVersionString))
        }
    }

    tasks.withType(org.gradle.api.tasks.testing.Test::class.java).configureEach {
        useJUnitPlatform()
    }
}

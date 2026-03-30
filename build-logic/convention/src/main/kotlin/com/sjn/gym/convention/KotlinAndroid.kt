package com.sjn.gym.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import org.gradle.api.artifacts.VersionCatalogsExtension

internal fun Project.configureKotlinAndroid(commonExtension: ExtensionAware) {
    val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
    val javaToolchainVersionString = libs.findVersion("javaToolchainVersion").get().toString()
    val javaTargetVersionString = libs.findVersion("javaTargetVersion").get().toString()
    val javaTargetVersion = JavaVersion.toVersion(javaTargetVersionString)

    if (commonExtension is ApplicationExtension) {
        commonExtension.compileOptions {
            sourceCompatibility = javaTargetVersion
            targetCompatibility = javaTargetVersion
        }
    } else if (commonExtension is LibraryExtension) {
        commonExtension.compileOptions {
            sourceCompatibility = javaTargetVersion
            targetCompatibility = javaTargetVersion
        }
    }

    // Configure Kotlin toolchain
    pluginManager.withPlugin(extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java).named("libs").findPlugin("kotlin-android").get().get().pluginId) {
        extensions.configure<KotlinAndroidProjectExtension> {
            jvmToolchain(javaToolchainVersionString.toInt())
        }
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
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaTargetVersionString))
        }
    }
}

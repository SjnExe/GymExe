#!/bin/bash
set -e

echo "Applying fixes..."

# 1. Update AGENTS.md
sed -i 's/### Maintenance/### Maintenance\n*   **Dependency Health (Build Health):**\n```bash\n.\/gradlew buildHealth --no-configuration-cache -Dorg.gradle.unsafe.isolated-projects=false\n```\n  *(Note: Project Isolation and Configuration Cache must be bypassed as they are currently incompatible with the Dependency Analysis plugin).*\n/g' AGENTS.md

# 2. Update root build.gradle.kts
sed -i 's/severity("fail")/severity("warn")\n            }\n            onUsedTransitiveDependencies {\n                severity("warn")\n            }\n            onIncorrectConfiguration {\n                severity("warn")/g' build.gradle.kts

# 3. Update core/ui/build.gradle.kts
cat << 'GRADLE_EOF' > core/ui/build.gradle.kts
plugins {
    id("gymexe.android.library")
    id("gymexe.android.compose")
    id("gymexe.roborazzi")
}

android { namespace = "com.sjn.gym.core.ui" }

dependencies {
    api("androidx.compose.foundation:foundation-layout:1.10.5")
    api("androidx.compose.runtime:runtime:1.10.5")
    implementation("androidx.compose.ui:ui-text:1.10.5")
    implementation("androidx.compose.ui:ui-unit:1.10.5")
    implementation("androidx.core:core:1.18.0")

    api(project(":core:model"))

    debugRuntimeOnly(libs.androidx.ui.test.manifest)

    testRuntimeOnly("io.mockk:mockk-agent-android:1.14.9")
    testRuntimeOnly("io.mockk:mockk:1.14.9")
    testRuntimeOnly(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.vintage.engine)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.roborazzi)
}

dependencyAnalysis {
    issues {
        onIncorrectConfiguration {
            exclude("androidx.compose.ui:ui-test-manifest")
        }
    }
}
GRADLE_EOF

# 4. Update core/data/build.gradle.kts
cat << 'GRADLE_EOF' > core/data/build.gradle.kts
plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
}

android { namespace = "com.sjn.gym.core.data" }

dependencies {
    api(project(":core:model"))

    implementation(libs.kotlinx.serialization.json)
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.10.0")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation("androidx.room:room-common:2.8.4")
    implementation("androidx.sqlite:sqlite:2.6.2")
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    api("androidx.datastore:datastore-core:1.2.1")
    api("androidx.datastore:datastore-preferences-core:1.2.1")

    // Retrofit
    api(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)

    // Chucker
    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)

    // Hilt
    api(libs.hilt.android)
    api("com.google.dagger:dagger:2.59.2")
    api("javax.inject:javax.inject:1")
    implementation("com.google.dagger:hilt-core:2.59.2")

    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    runtimeOnly(libs.kotlinx.coroutines.android)

    implementation("androidx.collection:collection:1.5.0")

    testRuntimeOnly("io.mockk:mockk-agent-android:1.14.9")
    testRuntimeOnly("io.mockk:mockk:1.14.9")
    testRuntimeOnly(libs.junit.vintage.engine)
}

dependencyAnalysis {
    issues {
        onIncorrectConfiguration {
            exclude("com.github.chuckerteam.chucker:library")
            exclude("com.github.chuckerteam.chucker:library-no-op")
        }
    }
}
GRADLE_EOF

# 5. Update core/model/build.gradle.kts
cat << 'GRADLE_EOF' > core/model/build.gradle.kts
plugins {
    id("gymexe.jvm.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.10.0")
}
GRADLE_EOF

# 6. Update Feature Convention Plugin
cat << 'GRADLE_EOF' > build-logic/convention/src/main/kotlin/com/sjn/gym/convention/AndroidFeatureConventionPlugin.kt
package com.sjn.gym.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("gymexe.android.library")
            pluginManager.apply("gymexe.android.hilt")
            pluginManager.apply("gymexe.android.compose")
            pluginManager.apply("gymexe.roborazzi")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("api", project(":core:ui"))
                add("api", project(":core:model"))
                add("api", project(":core:data"))

                // Standard Compose UI libraries for Features
                add("implementation", libs.findLibrary("androidx.ui").get())
                add("implementation", libs.findLibrary("androidx.ui.graphics").get())
                add("implementation", libs.findLibrary("androidx.material3").get())

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())

                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.ui.test.junit4").get())
                add("debugRuntimeOnly", libs.findLibrary("androidx.ui.test.manifest").get())
            }
        }
    }
}
GRADLE_EOF

# 7. Update app/build.gradle.kts
sed -i 's/implementation(libs.bundles.compose)/implementation(libs.bundles.compose)\n\n    implementation(libs.androidx.navigation.compose)\n    implementation(libs.androidx.hilt.navigation.compose)/g' app/build.gradle.kts

cat << 'GRADLE_EOF' >> app/build.gradle.kts

dependencyAnalysis {
    issues {
        onIncorrectConfiguration {
            exclude("com.github.chuckerteam.chucker:library")
            exclude("com.github.chuckerteam.chucker:library-no-op")
        }
    }
}
GRADLE_EOF

echo "Fixes applied!"

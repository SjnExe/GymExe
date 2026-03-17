plugins {
    id("gymexe.jvm.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.10.0")
}

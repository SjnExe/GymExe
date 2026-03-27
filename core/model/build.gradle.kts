plugins {
    id("gymexe.jvm.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.kotlinx.serialization.core)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
}

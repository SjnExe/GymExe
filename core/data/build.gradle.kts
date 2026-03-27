plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
}

android { namespace = "com.sjn.gym.core.data" }

configurations.maybeCreate("devDebugImplementation")

configurations.maybeCreate("stableDebugImplementation")

configurations.maybeCreate("devReleaseImplementation")

configurations.maybeCreate("stableReleaseImplementation")

configurations.maybeCreate("devBenchmarkImplementation")

configurations.maybeCreate("stableBenchmarkImplementation")

dependencies {
    testRuntimeOnly(libs.mockk.agent.android)
    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    api(project(":core:model"))
    api(libs.androidx.datastore.core)
    api(libs.androidx.datastore.preferences.core)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.core)
    api(libs.retrofit)

    implementation(libs.hilt.android)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.sqlite)
    implementation(libs.hilt.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    runtimeOnly(libs.kotlinx.coroutines.android)

    // Properly split Chucker variants
    "devDebugImplementation"(libs.chucker.debug)
    "stableDebugImplementation"(libs.chucker.debug)
    "devReleaseImplementation"(libs.chucker.release)
    "stableReleaseImplementation"(libs.chucker.release)
    "devBenchmarkImplementation"(libs.chucker.release)
    "stableBenchmarkImplementation"(libs.chucker.release)
}

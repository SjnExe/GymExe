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
    api(project(":core:model"))
    api(libs.androidx.datastore.core)
    api(libs.androidx.datastore.preferences.core)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.core)
    api(libs.retrofit)

    implementation(libs.androidx.collection)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    runtimeOnly(libs.kotlinx.coroutines.android)

    testRuntimeOnly(libs.bundles.junit.jupiter.runtime)
    testRuntimeOnly(libs.mockk.agent.android)

    "devBenchmarkImplementation"(libs.chucker.release)

    // Properly split Chucker variants
    "devDebugImplementation"(libs.chucker.debug)

    "devReleaseImplementation"(libs.chucker.release)

    ksp(libs.androidx.room.compiler)

    "stableBenchmarkImplementation"(libs.chucker.release)

    "stableDebugImplementation"(libs.chucker.debug)

    "stableReleaseImplementation"(libs.chucker.release)
}

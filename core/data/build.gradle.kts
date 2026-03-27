plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
}

android { namespace = "com.sjn.gym.core.data" }

configurations.maybeCreate("devDebugImplementation")

configurations.maybeCreate("devReleaseImplementation")

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
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
}

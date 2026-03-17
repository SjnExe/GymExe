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

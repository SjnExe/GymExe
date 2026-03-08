plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
    // Note: KSP and Kotlin Serialization are typically applied via your convention plugins
}

android {
    namespace = "com.sjn.gym.core.data"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit & Networking
    implementation(libs.retrofit)
    // Key Fix: The accessor order must match your TOML key 'retrofit-converter-kotlinx-serialization'
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)

    // Chucker - Use strings for flavor-based implementations to avoid configuration issues
    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}

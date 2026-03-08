plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
    // ksp plugin is added by hilt convention
}

android { namespace = "com.sjn.gym.core.data" }

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)

    // Chucker
    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)

    // Hilt handled by convention plugin (hilt-android, hilt-compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}

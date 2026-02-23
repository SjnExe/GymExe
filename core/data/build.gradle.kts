plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
    // ksp plugin is added by hilt convention
}

android {
    namespace = "com.sjn.gym.core.data"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp)

    // Chucker
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    // Hilt handled by convention plugin (hilt-android, hilt-compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}

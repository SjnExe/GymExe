plugins {
    id("gymexe.android.library")
    id("gymexe.android.hilt")
}

android { namespace = "com.sjn.gym.core.data" }

dependencies {
    api(project(":core:model"))
    api(libs.androidx.datastore.core)
    api(libs.androidx.datastore.preferences.core)

    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.core)

    api(libs.hilt.android)
    api(libs.retrofit)

    implementation(libs.androidx.collection)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.sqlite)
    implementation(libs.hilt.core)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)

    "devImplementation"(libs.chucker.debug)
    "stableImplementation"(libs.chucker.release)

    runtimeOnly(libs.kotlinx.coroutines.android)
}

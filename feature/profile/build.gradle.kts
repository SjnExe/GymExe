plugins { id("gymexe.android.feature") }

android { namespace = "com.sjn.gym.feature.profile" }

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
}

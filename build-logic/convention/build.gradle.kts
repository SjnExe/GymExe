plugins {
    `kotlin-dsl`
}

group = "com.sjn.gym.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gymexe.android.application"
            implementationClass = "com.sjn.gym.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "gymexe.android.library"
            implementationClass = "com.sjn.gym.convention.AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "gymexe.android.hilt"
            implementationClass = "com.sjn.gym.convention.AndroidHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "gymexe.android.compose"
            implementationClass = "com.sjn.gym.convention.AndroidComposeConventionPlugin"
        }
        register("spotless") {
            id = "gymexe.spotless"
            implementationClass = "com.sjn.gym.convention.SpotlessConventionPlugin"
        }
        register("detekt") {
            id = "gymexe.detekt"
            implementationClass = "com.sjn.gym.convention.DetektConventionPlugin"
        }
    }
}

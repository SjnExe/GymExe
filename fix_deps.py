import os

def replace_in_file(filepath, replacements):
    with open(filepath, 'r') as f:
        content = f.read()
    for search, replace in replacements:
        content = content.replace(search, replace)
    with open(filepath, 'w') as f:
        f.write(content)

replace_in_file('app/build.gradle.kts', [
    ('implementation(libs.androidx.ui.tooling.preview)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(libs.dagger)', 'api(libs.dagger)'),
    ('implementation(libs.javax.inject)', 'api(libs.javax.inject)'),
    ('"testImplementation"(libs.junit.jupiter)', '"testRuntimeOnly"(libs.junit.jupiter)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('debugImplementation(libs.leakcanary.android)', '"devDebugRuntimeOnly"(libs.leakcanary.android)'),
    ('debugRuntimeOnly(libs.androidx.ui.test.manifest)', '"devDebugRuntimeOnly"(libs.androidx.ui.test.manifest)\n    "stableDebugRuntimeOnly"(libs.androidx.ui.test.manifest)'),
    ('"devImplementation"(libs.chucker.debug)', '"devImplementation"(libs.chucker.debug)\n    "devDebugImplementation"(libs.chucker.debug)'),
    ('"stableImplementation"(libs.chucker.release)', '"stableImplementation"(libs.chucker.release)\n    "stableDebugImplementation"(libs.chucker.release)'),
    ('implementation(libs.kermit)', '''"devBenchmarkImplementation"(libs.kermit.core)
    "devDebugImplementation"(libs.kermit.android.debug)
    "devDebugImplementation"(libs.kermit.core.android.debug)
    "devReleaseImplementation"(libs.kermit.core)
    "stableBenchmarkImplementation"(libs.kermit.core)
    "stableDebugImplementation"(libs.kermit.android.debug)
    "stableDebugImplementation"(libs.kermit.core.android.debug)
    "stableReleaseImplementation"(libs.kermit.core)'''),
    ('implementation(libs.okhttp)', 'implementation(libs.okhttp)\n    implementation(libs.androidx.activity.compose)\n    implementation(libs.androidx.lifecycle.runtime.compose)\n    implementation(libs.androidx.lifecycle.viewmodel.compose)\n    implementation(libs.retrofit)\n    api(libs.kotlin.stdlib)'),
])

replace_in_file('core/data/build.gradle.kts', [
    ('implementation(libs.androidx.room.ktx)', ''),
    ('"testImplementation"(libs.junit)', ''),
    ('"testImplementation"(libs.junit.jupiter)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(libs.hilt.android)', 'api(libs.hilt.android)'),
    ('"devImplementation"(libs.chucker.debug)', '"devImplementation"(libs.chucker.debug)\n    "devDebugImplementation"(libs.chucker.debug)'),
    ('"stableImplementation"(libs.chucker.release)', '"stableImplementation"(libs.chucker.release)\n    "stableDebugImplementation"(libs.chucker.release)'),
    ('implementation(libs.kotlinx.coroutines.android)', 'runtimeOnly(libs.kotlinx.coroutines.android)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
])

replace_in_file('core/ui/build.gradle.kts', [
    ('"testImplementation"(libs.junit)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.roborazzi.core)', ''),
    ('"testImplementation"(libs.roborazzi.junit.rule)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('"testImplementation"(libs.junit.jupiter)', '"testRuntimeOnly"(libs.junit.jupiter)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('"testImplementation"(libs.robolectric)', '"testRuntimeOnly"(libs.robolectric)'),
    ('"testImplementation"(libs.roborazzi)', '"testRuntimeOnly"(libs.roborazzi)'),
    ('api(project(":core:model"))', 'api(project(":core:model"))\n    api(libs.androidx.ui)\n    implementation(libs.androidx.ui.graphics)'),
])

replace_in_file('feature/onboarding/build.gradle.kts', [
    ('implementation(libs.androidx.ui.tooling.preview)', ''),
    ('"testImplementation"(libs.junit)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.roborazzi.core)', ''),
    ('"testImplementation"(libs.roborazzi.junit.rule)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(project(":core:data"))', 'api(project(":core:data"))'),
    ('implementation(project(":core:model"))', 'api(project(":core:model"))'),
    ('"testImplementation"(libs.junit.jupiter)', '"testRuntimeOnly"(libs.junit.jupiter)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('"testImplementation"(libs.robolectric)', '"testRuntimeOnly"(libs.robolectric)'),
    ('"testImplementation"(libs.roborazzi)', '"testRuntimeOnly"(libs.roborazzi)'),
    ('implementation(libs.hilt.core)', 'implementation(libs.hilt.core)\n    implementation(libs.androidx.compose.foundation)\n    implementation(libs.androidx.lifecycle.viewmodel.compose)'),
])

replace_in_file('feature/profile/build.gradle.kts', [
    ('implementation(libs.androidx.ui.tooling.preview)', ''),
    ('"testImplementation"(libs.junit)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.roborazzi.core)', ''),
    ('"testImplementation"(libs.roborazzi.junit.rule)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(project(":core:data"))', 'api(project(":core:data"))'),
    ('implementation(project(":core:model"))', 'api(project(":core:model"))'),
    ('"testImplementation"(libs.junit.jupiter)', '"testRuntimeOnly"(libs.junit.jupiter)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('"testImplementation"(libs.robolectric)', '"testRuntimeOnly"(libs.robolectric)'),
    ('"testImplementation"(libs.roborazzi)', '"testRuntimeOnly"(libs.roborazzi)'),
    ('implementation(libs.hilt.core)', 'implementation(libs.hilt.core)\n    implementation(libs.androidx.compose.foundation)\n    implementation(libs.androidx.lifecycle.viewmodel.compose)'),
])

replace_in_file('feature/settings/build.gradle.kts', [
    ('implementation(libs.androidx.ui.tooling.preview)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(project(":core:data"))', 'api(project(":core:data"))'),
    ('implementation(project(":core:model"))', 'api(project(":core:model"))'),
    ('"testImplementation"(libs.junit.jupiter)', '"testRuntimeOnly"(libs.junit.jupiter)'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('implementation(libs.hilt.core)', 'implementation(libs.hilt.core)\n    implementation(libs.androidx.compose.foundation)\n    implementation(libs.androidx.lifecycle.runtime.compose)\n    implementation(libs.androidx.lifecycle.viewmodel.compose)'),
    ('implementation(libs.kermit)', '''"devDebugImplementation"(libs.kermit.android.debug)
    "devDebugImplementation"(libs.kermit.core.android.debug)
    "devReleaseImplementation"(libs.kermit.core)
    "stableDebugImplementation"(libs.kermit.android.debug)
    "stableDebugImplementation"(libs.kermit.core.android.debug)
    "stableReleaseImplementation"(libs.kermit.core)'''),
])

replace_in_file('feature/workout/build.gradle.kts', [
    ('implementation(libs.androidx.activity)', ''),
    ('implementation(libs.androidx.activity.compose)', ''),
    ('implementation(libs.androidx.ui.tooling.preview)', ''),
    ('implementation(project(":core:ui"))', ''),
    ('"testImplementation"(libs.junit)', ''),
    ('"testImplementation"(libs.mockk.android)', ''),
    ('"testImplementation"(libs.roborazzi.compose)', ''),
    ('"testImplementation"(libs.roborazzi.core)', ''),
    ('"testImplementation"(libs.roborazzi.junit.rule)', ''),
    ('"testImplementation"(libs.turbine)', ''),
    ('implementation(project(":core:data"))', 'api(project(":core:data"))'),
    ('implementation(project(":core:model"))', 'api(project(":core:model"))'),
    ('"testImplementation"(libs.mockk.agent.android)', '"testRuntimeOnly"(libs.mockk.agent.android)'),
    ('"testImplementation"(libs.robolectric)', '"testRuntimeOnly"(libs.robolectric)'),
    ('"testImplementation"(libs.roborazzi)', '"testRuntimeOnly"(libs.roborazzi)'),
    ('implementation(libs.hilt.core)', 'implementation(libs.hilt.core)\n    implementation(libs.androidx.compose.foundation)\n    implementation(libs.androidx.lifecycle.runtime.compose)\n    implementation(libs.androidx.lifecycle.viewmodel.compose)'),
])

replace_in_file('build-logic/convention/src/main/kotlin/com/sjn/gym/convention/AndroidFeatureConventionPlugin.kt', [
    ('add("implementation", project(":core:model"))', 'add("api", project(":core:model"))'),
    ('add("implementation", project(":core:data"))', 'add("api", project(":core:data"))'),
    ('add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())', ''),
])
replace_in_file('build-logic/convention/src/main/kotlin/com/sjn/gym/convention/AndroidTestConventionPlugin.kt', [
    ('add("testImplementation", libs.findBundle("testing-common").get())', 'add("testImplementation", libs.findLibrary("junit").get())\n                add("testImplementation", libs.findLibrary("mockk").get())\n                add("testRuntimeOnly", libs.findLibrary("mockk-agent-android").get())\n                add("testImplementation", libs.findLibrary("mockk-android").get())'),
    ('add("testImplementation", libs.findLibrary("junit-jupiter").get())', 'add("testRuntimeOnly", libs.findLibrary("junit-jupiter").get())'),
    ('add("testImplementation", libs.findLibrary("turbine").get())', 'add("testRuntimeOnly", libs.findLibrary("turbine").get())'),
])
replace_in_file('build-logic/convention/src/main/kotlin/com/sjn/gym/convention/RoborazziConventionPlugin.kt', [
    ('add("testImplementation", libs.findLibrary("robolectric").get())', 'add("testRuntimeOnly", libs.findLibrary("robolectric").get())'),
    ('add("testImplementation", libs.findBundle("roborazzi").get())', 'add("testRuntimeOnly", libs.findLibrary("roborazzi").get())\n                add("testRuntimeOnly", libs.findLibrary("roborazzi-compose").get())\n                add("testRuntimeOnly", libs.findLibrary("roborazzi-core").get())\n                add("testRuntimeOnly", libs.findLibrary("roborazzi-junit-rule").get())'),
])

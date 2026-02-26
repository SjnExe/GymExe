# Support for Room
-keep class androidx.room.RoomMasterTable {
    public static java.lang.String createInsertQuery(java.lang.String);
}
-keep class androidx.room.util.* { *; }

# Serialization
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @kotlinx.serialization.SerialName <fields>;
}

# Retrofit
-keepattributes Signature
-keepattributes Exceptions

# Data Binding
-keepclassmembers class * extends androidx.databinding.ViewDataBinding {
    public static * inflate(...);
    public static * bind(...);
    public <init>(...);
}

# Keep our core database entities
-keep class com.sjn.gym.core.data.database.GymDatabase_Impl { *; }
-keep class com.sjn.gym.core.model.** { *; }
-keep class com.sjn.gym.core.data.entity.** { *; }
-keep class com.sjn.gym.core.data.database.dao.** { *; }

# Keep Dagger/Hilt generated code
-keep class com.sjn.gym.GymExeApp_HiltComponents { *; }

# --- TEMPORARY DEBUGGING RULES FOR R8 CRASH ---
# Broadly keep serialization and networking libraries to identify the culprit.
# TODO: Narrow these down after the crash is resolved.

-keep class kotlinx.serialization.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }

# Preserve all Room Database implementations
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.RoomDatabase_Impl { *; }

# Fix for AndroidJUnitRunner crash on minified builds (devRelease)
# androidx.tracing.Trace is used by the runner but stripped by R8 if unused by the app.
-keep class androidx.tracing.** { *; }
# kotlin.LazyKt is used by androidx.test.platform.io.TestDirCalculator but stripped by R8
-keep class kotlin.LazyKt { *; }
# kotlin.time.Duration is used by AndroidComposeTestRule but methods like getINFINITE are stripped/renamed
-keep class kotlin.time.** { *; }
# kotlinx.coroutines.JobKt is used by androidx.compose.ui.test.IdlingResourceRegistry but stripped by R8
-keep class kotlinx.coroutines.** { *; }
# kotlin.coroutines.CoroutineContext$Key (and others) are used by AndroidComposeUiTestEnvironment
-keep class kotlin.coroutines.** { *; }
# kotlin.Result is used by the test environment but stripped by R8
-keep class kotlin.Result { *; }
-keep class kotlin.Result$Companion { *; }
-keep class kotlin.ResultKt { *; }
-keep class kotlin.Result** { *; }
# androidx.compose.ui.platform.InfiniteAnimationPolicy is used by AndroidComposeTestRule
-keep class androidx.compose.ui.platform.InfiniteAnimationPolicy { *; }
-keep class androidx.compose.ui.platform.InfiniteAnimationPolicy$DefaultImpls { *; }
# io.netty (used by gRPC in test runner infrastructure)
-keep class io.netty.** { *; }
# androidx.compose.runtime.MonotonicFrameClock$DefaultImpls is used by TestMonotonicFrameClock
-keep class androidx.compose.runtime.** { *; }

# Print configuration for debugging
-printconfiguration build/outputs/mapping/release/configuration.txt
-printusage build/outputs/mapping/release/usage.txt

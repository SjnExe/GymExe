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

# Print configuration for debugging
-printconfiguration build/outputs/mapping/release/configuration.txt
-printusage build/outputs/mapping/release/usage.txt

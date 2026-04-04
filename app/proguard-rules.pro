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

# Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Preserve all Room Database implementations
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.RoomDatabase_Impl { *; }

# NOTE: Test-specific rules are maintained in 'proguard-test-rules.pro'.
# That file is only applied to the 'dev' flavor for instrumented testing (connectedCheck).
# This file (proguard-rules.pro) contains rules required for the production app to run.

# Print configuration for debugging
-printconfiguration build/outputs/mapping/release/configuration.txt
-printusage build/outputs/mapping/release/usage.txt

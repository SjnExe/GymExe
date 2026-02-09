# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/lib/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Keep Room Entities (accessed via Reflection/Generated Code)
-keep class com.sjn.gymexe.data.local.entity.** { *; }

# Keep Data Models (used in Serialization/Backup)
-keep class com.sjn.gymexe.data.model.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontwarn kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }
-keep class * implements kotlinx.serialization.KSerializer { *; }

# Hilt/Dagger (Usually handled by plugin, but safety first)
-keep class com.sjn.gymexe.GymExeApp { *; }
-keep class com.sjn.gymexe.di.** { *; }

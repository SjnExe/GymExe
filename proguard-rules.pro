# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/lib/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# --- CRITICAL FIX FOR CRASH ---
# Keep all app code to prevent R8 from stripping generated classes (like GymDatabase_Impl)
# accessed via reflection.
-keep class com.sjn.gymexe.** { *; }

# Keep Room generated classes specifically (just in case)
-keep class * extends androidx.room.RoomDatabase

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontwarn kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }
-keep class * implements kotlinx.serialization.KSerializer { *; }

# Hilt/Dagger
-keep class com.sjn.gymexe.GymExeApp { *; }
-keep class com.sjn.gymexe.di.** { *; }

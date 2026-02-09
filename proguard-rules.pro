# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/lib/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# --- CRITICAL FIX FOR CRASH ---
# Keep all app code to prevent R8 from stripping generated classes (like GymDatabase_Impl)
# accessed via reflection.
-keep class com.sjn.gymexe.** { *; }

# Keep Room generated classes specifically, INCLUDING CONSTRUCTORS
# This fixes "NoSuchMethodException: GymDatabase_Impl.<init>"
-keep class * extends androidx.room.RoomDatabase {
    <init>();
}

# Defensive rule for the specific implementation class if package varies
-keep class **.GymDatabase_Impl {
    <init>();
}

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontwarn kotlinx.serialization.**
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }
-keep class * implements kotlinx.serialization.KSerializer { *; }

# Hilt/Dagger
-keep class com.sjn.gymexe.GymExeApp { *; }
-keep class com.sjn.gymexe.di.** { *; }

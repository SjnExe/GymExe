# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# Hilt
-keep class com.sjn.gymexe.Hilt_** { *; }
-keep interface com.sjn.gymexe.Hilt_** { *; }

# Room
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.RoomOpenHelper { *; }
-dontwarn androidx.room.paging.**

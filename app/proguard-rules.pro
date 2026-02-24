# Room
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

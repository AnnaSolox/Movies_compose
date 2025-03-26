# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

## ----------------------Begin: Retrofit2-------------

# Keeps all classes in the retrofit2 package and ensures they are not removed or obfuscated
-keep class retrofit2.** { *; }

# Keeps all classes in the api.models package and ensures they are not removed or obfuscated
-keep class com.example.movies.data.api.models.** { *; }

# Keeps classes that contain methods with Retrofit annotations (like @GET, @POST, etc.), ensuring they are not removed or obfuscated
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keeps method names of interfaces that contain Retrofit annotations, ensuring they are preserved for Retrofit to call
-keepclassmembernames interface * {
        @retrofit.http.* <methods>;
}

## ----------------------End: Retrofit2---------------

##-------------------------Begin:GSON-----------------

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------------End: Gson ---------------------

##------------------Begin: Room-----------------------

# Keeps classes that extend RoomDatabase
-keep class * extends androidx.room.RoomDatabase

# Keeps classes annotated with @Entity
-keep @androidx.room.Entity class *

# Keeps classes annotated with @Dao
-keep @androidx.room.Dao class *

##------------------End: Room------------------------

##--------------Begin: Kotlin Coroutines-------------

# Ensure that volatile fields are not removed or obfuscated
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

##---------------End: Kotlin Coroutines--------------
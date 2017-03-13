# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\bin\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,Signature

-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }

-keep class rx.internal.util.unsafe.** {
    *;
 }

-dontwarn rx.**
-dontwarn retrofit2.**
-dontwarn com.squareup.**
-dontwarn okio.**

-keep class retrofit2.** { *; }

-keep class sun.misc.Unsafe { *; }

-keep class s4fd.data.** { *; }

-keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
-keep class com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder
-keep class * extends com.raizlabs.android.dbflow.config.BaseDatabaseDefinition { *; }
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ice/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceF`ile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#gaode position
-libraryjars libs/jcore-android-1.1.6.jar
-libraryjars libs/jpush-android-3.0.8.jar
-libraryjars libs/aliyun-oss-sdk-android-2.3.0.jar
-libraryjars AliyunSdk-RC.aar
-libraryjars pgyer_sdk_2.7.0.jar
-keep class com.amap.api.location.** {*;}
-keep class com.amap.api.fence.** {*;}
-keep class com.autonavi.aps.amapapi.model.** {*;}
-keep class com.huawei.hms.** {*;}
-keep class com.huawei.hms.api.** {*;}
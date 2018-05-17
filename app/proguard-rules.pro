
-keepattributes Exceptions, Signature, InnerClasses
# Application config classes and ShellLibraryBuilder
-keep class com.screenz.shell_library.config.** { *; }
-keep class com.screenz.shell_library.model.** { *; }
-keep class com.screenz.shell_library.ShellLibraryBuilder { *; }

#Retrofit
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn okio.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

#WebView Native calls
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
#Picasso
-dontwarn com.squareup.okhttp.**

#New Relic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

#Comscore
-keep class com.comscore.** { *; }
-dontwarn com.comscore.**
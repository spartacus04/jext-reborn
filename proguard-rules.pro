-keep class !com.adierebel.**, !com.adierebel.** { *; }
-keepclasseswithmembers public class com.javafx.main.Main, org.eclipse.jdt.internal.jarinjarloader.*, com.adierebel.MainKt {
    public static void main(java.lang.String[]);
}

-dontwarn net.minecraft.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn org.apache.**
-dontwarn com.sun.**
-dontwarn com.j256.ormlite.**
-dontwarn com.google.protobuf.**
-dontwarn com.google.common.**
-dontwarn com.mysql.**
-dontwarn java.util.**
-dontwarn javax.imageio.**
-dontwarn javax.swing.plaf.**
-dontwarn javax.xml.**
-dontwarn java.sql.**

-ignorewarnings

# GSON
-keepattributes Signature
-keepattributes Signature
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes *Annotation*,EventHandler
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
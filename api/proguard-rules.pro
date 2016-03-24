# Api
-dontobfuscate

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep class com.makingiants.today.api.error_handling.exception.** { *; }

-keep public class com.makingiants.today.api.** { public protected *;}

-keepclassmembernames class com.makingiants.today.api.** {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames class com.makingiants.today.api.** { native <methods>; }

-keepclassmembers enum com.makingiants.today.api.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class com.makingiants.today.api.** implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okio
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

## Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*
#-keep class com.google.gson.stream.** { *; }

# Joda
-keep class org.joda.time.DateTime { public static *; }

# kotlin
-dontwarn kotlin.**

# Timber
-assumenosideeffects class timber.log.Timber* {
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
}
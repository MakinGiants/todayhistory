-optimizations code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Keep Parcelable classes
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# ultrarecyclerview
-dontwarn co.nullindustries.ultrarecyclerview.SwipeDismissRecyclerViewTouchListener*

# Craslytics https://dev.twitter.com/crashlytics/android/proguard-dexguard
-keepattributes SourceFile,LineNumberTable
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Fabric
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile,LineNumberTable,*Annotation*

# Picasso
-dontwarn com.squareup.okhttp.**

# kotlin
-dontwarn kotlin.**

# Anko
-dontwarn org.jetbrains.anko.**
# Default ProGuard rules for SmartReader
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# DataStore
-keep class androidx.datastore.** { *; }

# Navigation
-keep class androidx.navigation.** { *; }

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Material Components
-keep class com.google.android.material.** { *; }

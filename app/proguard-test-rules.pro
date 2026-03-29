# Fix for AndroidJUnitRunner crash on minified builds (devRelease)
# androidx.tracing.Trace is used by the runner but stripped by R8 if unused by the app.
-keep class androidx.tracing.** { *; }

# kotlin.LazyKt is used by androidx.test.platform.io.TestDirCalculator but stripped by R8
-keep class kotlin.LazyKt { *; }

# kotlin.time.Duration is used by AndroidComposeTestRule but methods like getINFINITE are stripped/renamed
-keep class kotlin.time.** { *; }

# kotlinx.coroutines.JobKt is used by androidx.compose.ui.test.IdlingResourceRegistry but stripped by R8
-keep class kotlinx.coroutines.** { *; }

# kotlin.coroutines.CoroutineContext$Key (and others) are used by AndroidComposeUiTestEnvironment
-keep class kotlin.coroutines.** { *; }

# kotlin.Result is used by the test environment but stripped by R8
-keep class kotlin.Result { *; }
-keep class kotlin.Result$Companion { *; }
-keep class kotlin.ResultKt { *; }
-keep class kotlin.Result** { *; }

# kotlin.jvm.internal.FunctionReferenceImpl and other internal classes are used by TestScope
-keep class kotlin.jvm.internal.** { *; }

# androidx.compose.ui.platform.InfiniteAnimationPolicy is used by AndroidComposeTestRule
-keep class androidx.compose.ui.platform.InfiniteAnimationPolicy { *; }
-keep class androidx.compose.ui.platform.InfiniteAnimationPolicy$DefaultImpls { *; }

# io.netty (used by gRPC in test runner infrastructure)
-keep class io.netty.** { *; }

# javax.inject.Provider is used by Dagger/Hilt and can be stripped in R8 full mode
-keep interface javax.inject.Provider { *; }

# com.google.common.util.concurrent.ListenableFuture is used by AndroidX Test libraries
-keep class com.google.common.util.concurrent.ListenableFuture { *; }

# androidx.compose.runtime.MonotonicFrameClock$DefaultImpls is used by TestMonotonicFrameClock
-keep class androidx.compose.runtime.** { *; }

# androidx.compose.ui.platform.ViewRootForTest is used by ComposeRootRegistry during teardown
-keep class androidx.compose.ui.platform.ViewRootForTest { *; }

# Fix for NoSuchMethodError: setOnViewCreatedCallback in instrumented tests
# Preserve Compose platform internals used by the test runner
-keep class androidx.compose.ui.platform.** { *; }
-keep class androidx.compose.ui.test.** { *; }

# Fix for NoClassDefFoundError: ViewTreeLifecycleOwner in instrumented tests
# androidx.lifecycle.ViewTreeLifecycleOwner is used by ComposeRootRegistry$StateChangeHandler
-keep class androidx.lifecycle.ViewTreeLifecycleOwner { *; }
-keep class androidx.lifecycle.ViewTreeViewModelStoreOwner { *; }
-keep class androidx.savedstate.ViewTreeSavedStateRegistryOwner { *; }

# Fix for NoClassDefFoundError: kotlin.collections.CollectionsKt in instrumented tests
# Used by ComposeRootRegistry dispatchOnRegistrationChanged
-keep class kotlin.collections.** { *; }

# Fix for ClassNotFoundException: com.sjn.gym.GymExeApp in benchmark tests
# Explicitly preserve the Application class and its Hilt base class
-keep class com.sjn.gym.GymExeApp { *; }
-keep class com.sjn.gym.Hilt_GymExeApp { *; }

# Keep any classes that extend Android Application
-keep class * extends android.app.Application {
    <init>();
    void attachBaseContext(android.content.Context);
}

# Keep Hilt entry points and generated classes required for dependency injection
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.EntryPoint class *
-keep class com.sjn.gym.**_GeneratedInjector { *; }
-keep class com.sjn.gym.GymExeApp_HiltComponents** { *; }

# Also keep the Hilt classes in the .dev sub-package if they end up there due to R8/Flavor interactions
-keep class com.sjn.gym.dev.GymExeApp { *; }
-keep class com.sjn.gym.dev.Hilt_GymExeApp { *; }
-keep class com.sjn.gym.dev.**_GeneratedInjector { *; }
-keep class com.sjn.gym.dev.GymExeApp_HiltComponents** { *; }

# Preserve test instrument runner application package rules
-keep class com.sjn.gym.dev.test.** { *; }
-keep class com.sjn.gym.test.** { *; }

# Preserve test runners and test components inside the minified test apk
-keep class androidx.test.** { *; }
-keep class org.junit.** { *; }
-keep class junit.** { *; }

# Preserve anything required for Dagger/Hilt to work in full mode
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# NOTE: This file contains ProGuard rules SPECIFIC to instrumented tests.
# These rules are only applied to the 'dev' flavor to prevent test crashes
# when running against minified builds (R8 full mode).
# Rules required for the production app should go in 'proguard-rules.pro'.

# Keep Kotlin stdlib for instrumented tests
-keep class kotlin.** { *; }
-dontwarn kotlin.**

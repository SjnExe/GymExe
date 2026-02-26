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

# NOTE: This file contains ProGuard rules SPECIFIC to instrumented tests.
# These rules are only applied to the 'dev' flavor to prevent test crashes
# when running against minified builds (R8 full mode).
# Rules required for the production app should go in 'proguard-rules.pro'.

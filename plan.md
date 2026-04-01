# GymExe Development Plan & Handover Status

## 1. Project Overview
**App Name:** GymExe
**Package Name:** com.sjn.gym
**Purpose:** A modular, offline-first workout tracker for gym goers.
**Target OS:** Android (Min SDK 26, Target SDK 36).
**Philosophy:** Modular architecture, latest stable tools, absolute control over data (backups, logs), and "intelligent" input methods.

## 2. Environment & Tooling (Implemented)
*   **Language:** Kotlin 2.3.20
*   **JDK:** Java 25 (OpenJDK). Build logic uses Java 25 Toolchain.
*   **Build System:** Gradle 9.4.1 (Wrapper included)
*   **Android SDK:** Compile 36, Min 26.
*   **UI Framework:** Jetpack Compose (Material 3).
*   **Dependency Injection:** Hilt.
*   **Database:** Room (SQLite).
*   **CI/CD:** GitHub Actions (Build, Test, Release).
*   **Code Quality:** Spotless (Ktlint), Android Lint.
*   **Dev Tools:** Chucker (Network Inspection), Timber (Logging), LeakCanary.

## 3. Architecture (Implemented)
The app follows a fully modular architecture optimized with convention plugins (`build-logic`), Gradle Isolated Projects, and Configuration Cache for faster builds and easier debugging:
*   `:app`: Main entry point, navigation graph, flavor configuration.
*   `:core:ui`: Design system, themes (GymExeTheme), common components.
*   `:core:data`: Room Database, Repository implementations, DataStore.
*   `:core:model`: Domain entities (Exercise, Workout, etc.).
*   `:feature:settings`: Settings screens (Theme, Units, Backup placeholders).
*   `:feature:workout`: Workout logging, Intelligent Text Box (PlateCalculator), Exercise list.
*   `:feature:profile`: "You" page placeholders.
*   `:feature:onboarding`: Setup wizard flow.

## 4. CI/CD Workflow (Implemented & Stabilized)
**File:** `.github/workflows/build.yml`
**Features:**
*   **JDK 25 Setup**: Uses Temurin distribution.
*   **Caching**: Gradle build/cache restored effectively. AVD caching (API 36).
*   **Signing**: Uses repo-stored `app/debug.keystore` with default `android` credentials for simplicity.
*   **Versioning**:
    *   **Stable**: Tag `v*` -> Version `1.2.3`.
    *   **Dev**: Hardcoded to `0.0.1` (versionName) and `1` (versionCode) to preserve configuration cache.
    *   **Fallback**: Defaults to `0.0.1` if no tags exist.
*   **Flavors**: Builds `dev` (debuggable, suffixed) and `stable` (minified) variants.
*   **Artifacts**:
    *   `GymExe-dev-release.apk` / `GymExe-dev-debug.apk` (Dev builds).
    *   `GymExe-stable-release.apk` (Stable builds).
*   **Emulator**: Uses `api-level: 36`.

## 5. Build Logic (Implemented Infrastructure)
**Directory:** `build-logic/`
**Purpose:** Share build configuration via Convention Plugins.
**Plugins Available:**
*   `gymexe.android.application`: Common Android App config (SDK versions, Kotlin options).
*   `gymexe.android.library`: Common Android Library config.
*   `gymexe.android.compose`: Jetpack Compose setup.
*   `gymexe.android.hilt`: Hilt/KSP setup.
*   `gymexe.android.feature`: Feature module config (aggregates library, hilt, compose).
*   `gymexe.spotless`: Spotless configuration.

## 6. Handover Checklist & Status

### Phase 1: Repository & Environment Setup (Completed)
- [x] **Initialize Repository**: `README.md`, `LICENSE`, `.gitignore` created.
- [x] **AGENTS.md**: Comprehensive agent instructions added.
- [x] **Gradle Setup**: `gradle.properties` (JVM args, optimizations), `libs.versions.toml` configured.
- [x] **Signing**: `debug.keystore` generated and tracked. `build.gradle.kts` uses it.
- [x] **CI/CD Pipeline**: `build.yml` robust, correct versioning, artifact upload fixed.
- [x] **Java 25 Upgrade**: Toolchains configured, Gradle plugins updated.
- [x] **Strict Linting**: Spotless applied, Android lint enabled.

### Phase 2: Core Architecture & UI Foundation (Completed)
- [x] **Icons**: Vector Assets (Adaptive) created. New Splash Icon created.
- [x] **Theming Engine**: Material 3, Dark/Light/System mode, Dynamic Color support.
- [x] **Navigation**: Type-safe Compose Navigation set up (`GymExeNavHost`).
- [x] **Build Logic**: Infrastructure created.
- [x] **Splash Screen**: Implemented `androidx.core.splashscreen` for instant startup.

### Phase 3: Onboarding (Setup Wizard) (Completed)
- [x] **Welcome Screen**: Basic UI implemented.
- [x] **DataStore**: `UserPreferencesRepository` stores onboarding status.
- [x] **Profile Setup**: Input fields for gender/height/weight.
- [x] **Experience Level**: Selection UI implemented.
- [x] **Equipment Setup**: Selection UI implemented.

### Phase 4: Settings Feature (Partially Implemented)
- [x] **Theme Settings**: Functional UI for switching themes (System/Light/Dark).
- [x] **Settings UI**: "Hexium" style implementation (Segmented Buttons, Developer Options).
- [x] **Updater**:
    *   **Stable**: Checks latest tag, prioritizes ABI-specific APKs.
    *   **Dev**: Checks dev tag changelog, forces `dev-release`.
    *   **UI**: Dialog shows version diff, changelog, and download progress/size.
- [x] **Backup & Restore**: Functional integration with `BackupRepository` (File picker/saver).
- [x] **Unit Configuration**: Metric/Imperial toggle (Weight: kg/lbs, Dist: km/mi, Size: cm/in).
- [x] **Developer Options**: Copy Logs / Save Logs, Network Inspector launch.
- [x] **First Day of Week**: Auto-detect from Calendar or manual override.
- [x] **Clear Logs**: Feature added to wipe logs from device.

### Phase 5: Data Layer (Room) (Implemented)
- [x] **Entities**: `ExerciseEntity` created (with instructions). `RoutineEntity` and `WorkoutPlanEntity` created.
- [x] **Database**: `GymDatabase` configured with pre-population callback.
- [x] **DAO**: `ExerciseDao` implemented.
- [x] **Migrations**: Logic for merging user vs built-in data needed later.

### Phase 6: Workout & Intelligent Input (Implemented)
- [x] **Intelligent Text Box**: `PlateCalculator` and `WeightInputParser` implemented and tested.
- [x] **Auto-Merger**: Logic to merge weights (e.g., `5 5` -> `2x5`) for Stackable equipment.
- [x] **Strict Mode**: Validation for non-stackable equipment (Dumbbells/Selectorized Machines).
- [x] **UI Integration**: `WorkoutScreen` connects input to parser and validation.
- [x] **Syntax Highlighting**: Text box colors Quantity, Operator, and Weight.
- [x] **Library UI**: Revamped. Routines tab prioritized and fully implemented with lists. Exercises tab accessible alongside it.
- [x] **Exercise Detail**: UI skeleton implemented.
- [x] **Workout Session**:
    *   **Sets**: Types (Warmup, Failure, Drop set, etc.).
    *   **Timers**: Quick timer, Rest timer.
    *   **Notifications**: Ongoing workout notification.

### Phase 7: Profile & Stats (Partially Implemented)
- [x] **Basic Screen**: UI with revamped Height Input supporting Feet & Inches, and structured sections.
- [x] **Graphs**: Weight History Line Chart UI implemented in Canvas, ready for real data.

### Phase 8: Stability & Debugging (New)
- [x] **R8 Support**: ProGuard rules added for Room, Serialization, and Retrofit to support `android.enableR8.fullMode=true`.
- [x] **Global Crash Handler**: `CrashActivity` catches uncaught exceptions in `dev` builds and offers log sharing.
- [x] **Robust Logging**: `LogRepository` is now synchronized. `build.yml` captures CI logs efficiently.
- [x] **CI Optimization**: Removed redundant ARM64 tests and reduced log verbosity.
- [x] **Instrumented Tests on R8 builds**: Implemented specific, targeted ProGuard rules (`androidx.tracing`, `kotlin.time`, `kotlin.collections`, etc.) via a dedicated `benchmark` build type.

## 7. Detailed Instructions for Next Session

1.  **Instrumented Test Stability**:
    *   **Architecture:** The project now uses a `benchmark` build type (inheriting from `release`) specifically for CI instrumented tests. This build type applies `app/proguard-test-rules.pro` to prevent test runner crashes while keeping R8 full mode enabled.
    *   **Workflow:**
        *   **Manual QA:** Build/Install `devRelease`. This is a strict R8 build (identical to production `stableRelease`).
        *   **CI Testing:** The CI runs `connectedDevBenchmarkAndroidTest`. This uses the `benchmark` build type which includes the necessary keep rules for the test harness.
    *   **Constraint:** Instrumented tests currently require a CI environment with KVM (hardware acceleration) support. The local agent environment does NOT support KVM, so tests must be verified via GitHub Actions logs.

2.  **Fix AGP/KSP + JUnit 5 `failOnNoDiscoveredTests` Issue (Completed)**:
    *   **Fix:** Resolved natively via Gradle build logic configuration. Configured JUnit Platform with `testTask.setProperty("failOnNoDiscoveredTests", false)` and `testTask.systemProperty("junit.jupiter.execution.failIfNoTests", "false")`. The `DummyTest.kt` files have been removed from the repository.

3.  **Routines & Scheduling Logic (Completed)**:
    *   Implement CRUD operations for `Routine` and `WorkoutPlan`.
    *   Connect UI to `RoutineRepository` (needs creation).
    *   Implement logic to "Activate" a routine and reflect it on Home Screen.

4.  **Exercise Data Population (Completed)**:
    *   Add real data for `instructions` and more exercises.

## 8. Improvements & Optimizations (Backlog)

### Build Logic & Infrastructure
- [x] **Feature Convention Plugin**: Create a plugin to aggregate `android-library`, `android-compose`, and `android-hilt` for feature modules to reduce `build.gradle.kts` boilerplate.
- [x] **JVM Library Convention Plugin**: Create a plugin for pure Kotlin modules (e.g., `:core:model`) to avoid Android overhead where unnecessary.
- [x] **Roborazzi Integration**: Create a convention plugin to standardize screenshot testing configuration across UI modules.
- [x] **Kover Integration**: Set up code coverage aggregation for the entire project.

### Developer Experience (DX)
- [x] **Setup Script**: Integrated into `AGENTS.md` for centralized environment initialization.
- [x] **Dependency Bundling**: Group related dependencies in `libs.versions.toml` (e.g., `compose-ui`, `unit-test`) for cleaner build files.

### Code Quality & Architecture
- [x] **Konsist Tests**: Integrated `Konsist` to enforce architectural rules (e.g., ViewModels must reside in feature packages).

### CI/CD Enhancements
- [x] **Roborazzi on CI**: Ensure screenshot tests run and upload artifacts on failure.
- [ ] **Release Notes Generation**: Can still be written manually for now, or automate further with Release Drafter later.
- [x] **Instrumented Tests**: Add Emulator-based tests (`managed devices`) to catch runtime crashes on CI.

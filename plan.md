# GymExe Development Plan & Handover Status

## 1. Project Overview
**App Name:** GymExe
**Package Name:** com.sjn.gym
**Purpose:** A modular, offline-first workout tracker for gym goers.
**Target OS:** Android (Min SDK 26, Target SDK 36).
**Philosophy:** Modular architecture, latest stable tools, absolute control over data (backups, logs), and "intelligent" input methods.

## 2. Environment & Tooling (Implemented)
*   **Language:** Kotlin
*   **JDK:** Java 25 (OpenJDK)
*   **Build System:** Gradle 9.3.1 (Wrapper included)
*   **Android SDK:** Compile 36, Min 26.
*   **UI Framework:** Jetpack Compose (Material 3).
*   **Dependency Injection:** Hilt.
*   **Database:** Room (SQLite).
*   **CI/CD:** GitHub Actions (Build, Test, Release).

## 3. Architecture (Implemented)
The app follows a modular architecture:
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
*   **Caching**: Gradle build/cache restored effectively.
*   **Signing**: Uses repo-stored `app/debug.keystore` with default `android` credentials for simplicity (Secrets removed).
*   **Versioning**:
    *   **Stable**: Tag `v*` -> Version `1.2.3`.
    *   **Dev**: `v{LatestTag}-dev-PR{Num}.{Commits}` or `v{LatestTag}-dev-run{Num}`.
    *   **Fallback**: Defaults to `v0.0.0` if no tags exist.
    *   **Version Code**: `Total Commits - 1`.
*   **Releases**:
    *   **Dev**: Deletes floating `dev` tag/release and recreates it with new APKs (individual assets, no zip).
    *   **Stable**: Creates standard release.
    *   **Release Notes**: Uses specific format for parsing: `Development build triggered by {method}.\nVersion: {version_string}`.
*   **Flavors**: Builds `dev` (debuggable, suffixed) and `stable` (minified) variants.
*   **Artifacts**: Uploads `gym-exe-dev-{version}` (uncompressed zip artifact).

## 5. Build Logic (Implemented Infrastructure)
**Directory:** `build-logic/`
**Purpose:** Share build configuration via Convention Plugins.
**Plugins Available:**
*   `gymexe.android.application`: Common Android App config (SDK versions, Kotlin options).
*   `gymexe.android.library`: Common Android Library config.
*   `gymexe.android.compose`: Jetpack Compose setup.
*   `gymexe.android.hilt`: Hilt/KSP setup.
**Next Step:** Migrate module `build.gradle.kts` files to use `plugins { id("gymexe.android.application") ... }`.

## 6. Handover Checklist & Status

### Phase 1: Repository & Environment Setup (Completed)
- [x] **Initialize Repository**: `README.md`, `LICENSE`, `.gitignore` created.
- [x] **AGENTS.md**: Comprehensive agent instructions added.
- [x] **Gradle Setup**: `gradle.properties` (JVM args, optimizations), `libs.versions.toml` configured.
- [x] **Signing**: `debug.keystore` generated and tracked. `build.gradle.kts` uses it.
- [x] **CI/CD Pipeline**: `build.yml` robust, correct versioning, artifact upload fixed.

### Phase 2: Core Architecture & UI Foundation (Completed)
- [x] **Icons**: Vector Assets (Adaptive) created.
- [x] **Theming Engine**: Material 3, Dark/Light/System mode, Dynamic Color support.
- [x] **Navigation**: Type-safe Compose Navigation set up.
- [x] **Build Logic**: Infrastructure created.

### Phase 3: Onboarding (Setup Wizard) (Partially Implemented)
- [x] **Welcome Screen**: Basic UI implemented.
- [x] **DataStore**: `UserPreferencesRepository` stores onboarding status.
- [ ] **Profile Setup**: Input fields for gender/height/weight (UI needed).
- [ ] **Experience Level**: Selection UI needed (Free style, Fixed Day/Week, Splits).
- [ ] **Equipment Setup**: Selection UI needed (None, Full Gym, Specific Machines/Weights).

### Phase 4: Settings Feature (Partially Implemented)
- [x] **Theme Settings**: Functional UI for switching themes.
- [ ] **Updater**: Logic to check `dev` release notes using the format `Version: ...` is pending.
- [ ] **Backup & Restore**:
    *   **Format**: `.gym` file (compressed ZIP containing DB + Prefs).
    *   **Location**: `Documents/GymExe`.
    *   **Restore**: Two modes (Auto-detect / Manual Select).
- [ ] **Unit Configuration**: Metric/Imperial toggle (Weight: kg/lbs, Dist: km/mi, Size: cm/in).
- [ ] **Developer Options**: Copy Logs / Save Logs.
- [ ] **First Day of Week**: Auto-detect from Calendar or manual override.

### Phase 5: Data Layer (Room) (Implemented)
- [x] **Entities**: `ExerciseEntity` created.
- [x] **Database**: `GymDatabase` configured with pre-population callback.
- [x] **DAO**: `ExerciseDao` implemented.
- [ ] **Migrations**: Logic for merging user vs built-in data needed later.

### Phase 6: Workout & Intelligent Input (Partially Implemented)
- [x] **Intelligent Text Box**: `PlateCalculator` logic implemented and unit-tested.
- [x] **UI Integration**: `WorkoutScreen` connects input to calculator.
- [ ] **Syntax Highlighting**: Text box needs colored syntax for Quantity, Operator, and Weight.
- [x] **Exercise List**: `ExerciseListScreen` displays data from DB.
- [ ] **Workout Session**:
    *   **Sets**: Types (Warmup, Failure, Drop set, etc.).
    *   **Timers**: Quick timer, Rest timer.
    *   **Notifications**: Ongoing workout notification.

### Phase 7: Profile & Stats (Placeholder)
- [x] **Basic Screen**: Placeholder UI.
- [ ] **Graphs**: Charts and stats logic needed.

## 7. Detailed Instructions for Next Session

1.  **Migrate to Convention Plugins**:
    *   Refactor `app/build.gradle.kts`: Use `id("gymexe.android.application")`, `id("gymexe.android.compose")`, `id("gymexe.android.hilt")`.
    *   Refactor `core/ui`: `gymexe.android.library`, `gymexe.android.compose`.
    *   Refactor `core/data`: `gymexe.android.library`, `gymexe.android.hilt`.
    *   Refactor `feature/*`: `gymexe.android.library`, `gymexe.android.compose`, `gymexe.android.hilt`.

2.  **Updater Logic**:
    *   Fetch `https://api.github.com/repos/SjnExe/GymExe/releases/tags/dev`.
    *   Parse `body` to find line starting with `Version: `.
    *   Compare with `BuildConfig.VERSION_NAME`.
    *   Show Update UI if newer.

3.  **Backup Implementation**:
    *   Create `BackupManager`.
    *   Zip `getDatabasePath("gym_database.db")` and shared prefs XML.
    *   Handle `Documents/GymExe` permissions (SAF or standard storage depending on Android version).

4.  **Workout UI Polish**:
    *   Implement `AnnotatedString` building for Syntax Highlighting in `WorkoutScreen` text field.

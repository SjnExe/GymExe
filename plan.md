# GymExe Development Plan & Handover Status

## 1. Project Overview
**App Name:** GymExe
**Package Name:** com.sjn.gym
**Purpose:** A modular, offline-first workout tracker for gym goers.
**Target OS:** Android (Min SDK 26, Target SDK 36).
**Philosophy:** Modular architecture, latest stable tools, absolute control over data (backups, logs), and "intelligent" input methods.

## 2. Environment & Tooling (Implemented)
*   **Language:** Kotlin 2.3.10
*   **JDK:** Java 25 (OpenJDK). Build logic uses Java 25 Toolchain.
*   **Build System:** Gradle 9.3.1 (Wrapper included)
*   **Android SDK:** Compile 36, Min 26.
*   **UI Framework:** Jetpack Compose (Material 3).
*   **Dependency Injection:** Hilt.
*   **Database:** Room (SQLite).
*   **CI/CD:** GitHub Actions (Build, Test, Release).
*   **Code Quality:** Spotless (Ktlint), Detekt (Strict Mode with `warningsAsErrors`), Android Lint.
*   **Dev Tools:** Chucker (Network Inspection), Timber (Logging), LeakCanary.

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
*   **Signing**: Uses repo-stored `app/debug.keystore` with default `android` credentials for simplicity.
*   **Versioning**:
    *   **Stable**: Tag `v*` -> Version `1.2.3`.
    *   **Dev**: `v{LatestTag}-dev-PR{Num}.{Commits}` or `v{LatestTag}-dev-run{Num}`.
    *   **Fallback**: Defaults to `v0.0.0` if no tags exist.
    *   **Version Code**: `Total Commits - 1`.
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
*   `gymexe.detekt`: Detekt configuration (Strict).
*   `gymexe.spotless`: Spotless configuration.

## 6. Handover Checklist & Status

### Phase 1: Repository & Environment Setup (Completed)
- [x] **Initialize Repository**: `README.md`, `LICENSE`, `.gitignore` created.
- [x] **AGENTS.md**: Comprehensive agent instructions added.
- [x] **Gradle Setup**: `gradle.properties` (JVM args, optimizations), `libs.versions.toml` configured.
- [x] **Signing**: `debug.keystore` generated and tracked. `build.gradle.kts` uses it.
- [x] **CI/CD Pipeline**: `build.yml` robust, correct versioning, artifact upload fixed.
- [x] **Java 25 Upgrade**: Toolchains configured, Gradle plugins updated.
- [x] **Strict Linting**: Spotless applied, Detekt strict mode enabled.

### Phase 2: Core Architecture & UI Foundation (Completed)
- [x] **Icons**: Vector Assets (Adaptive) created.
- [x] **Theming Engine**: Material 3, Dark/Light/System mode, Dynamic Color support.
- [x] **Navigation**: Type-safe Compose Navigation set up (`GymExeNavHost`).
- [x] **Build Logic**: Infrastructure created.

### Phase 3: Onboarding (Setup Wizard) (Partially Implemented)
- [x] **Welcome Screen**: Basic UI implemented.
- [x] **DataStore**: `UserPreferencesRepository` stores onboarding status.
- [ ] **Profile Setup**: Input fields for gender/height/weight (UI needed).
- [ ] **Experience Level**: Selection UI needed (Free style, Fixed Day/Week, Splits).
- [ ] **Equipment Setup**: Selection UI needed (None, Full Gym, Specific Machines/Weights).

### Phase 4: Settings Feature (Partially Implemented)
- [x] **Theme Settings**: Functional UI for switching themes (System/Light/Dark).
- [x] **Settings UI**: "Hexium" style implementation (Segmented Buttons, Developer Options).
- [x] **Updater**: Mock logic implemented. Shows version from BuildConfig.
- [x] **Backup & Restore**: Functional integration with `BackupRepository` (File picker/saver).
- [x] **Unit Configuration**: Metric/Imperial toggle (Weight: kg/lbs, Dist: km/mi, Size: cm/in).
- [x] **Developer Options**: Copy Logs / Save Logs, Network Inspector launch.
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
    *   Refactor module `build.gradle.kts` files to use the convention plugins.

2.  **Onboarding Flow Completion**:
    *   Implement screens for Profile Setup, Experience Level, and Equipment Selection.
    *   Connect to `UserPreferencesRepository`.

3.  **Workout UI Polish**:
    *   Implement `AnnotatedString` building for Syntax Highlighting in `WorkoutScreen` text field.
    *   Improve Exercise List UI.

4.  **Database Migration**:
    *   Handle data persistence during updates.

## 8. Improvements & Optimizations (Backlog)

### Build Logic & Infrastructure
- [ ] **Feature Convention Plugin**: Create a plugin to aggregate `android-library`, `android-compose`, and `android-hilt` for feature modules to reduce `build.gradle.kts` boilerplate.
- [ ] **JVM Library Convention Plugin**: Create a plugin for pure Kotlin modules (e.g., `:core:model`) to avoid Android overhead where unnecessary.
- [ ] **Roborazzi Integration**: Create a convention plugin to standardize screenshot testing configuration across UI modules.
- [ ] **Kover Integration**: Set up code coverage aggregation for the entire project.

### Developer Experience (DX)
- [x] **Setup Script**: Integrated into `AGENTS.md` for centralized environment initialization.
- [ ] **Pre-Push Hook**: Create `scripts/pre-push.sh` to run critical checks locally before pushing.
- [ ] **Dependency Bundling**: Group related dependencies in `libs.versions.toml` (e.g., `compose-ui`, `unit-test`) for cleaner build files.

### Code Quality & Architecture
- [ ] **Konsist Tests**: Integrate `Konsist` to enforce architectural rules (e.g., "Repositories must reside in `data` package").
- [ ] **Detekt Refinement**: Review strict rules (e.g., `MagicNumber` for UI definitions) and adjust excludes.
- [ ] **Module Graph**: Automate generation of Mermaid diagrams for `README.md` using the `moduleGraph` plugin.

### CI/CD Enhancements
- [ ] **Roborazzi on CI**: Ensure screenshot tests run and upload artifacts on failure.
- [ ] **Release Drafter**: Consider automating release notes generation further.

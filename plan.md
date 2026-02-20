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
    *   **Dev**: `v0.0.1-dev-PR{num}.{commits}` or `v0.0.1-dev-run{num}`.
    *   Version Code: `Total Commits - 1`.
*   **Releases**:
    *   **Dev**: Deletes floating `dev` tag/release and recreates it with new APKs (individual assets, no zip).
    *   **Stable**: Creates standard release.
*   **Flavors**: Builds `dev` (debuggable, suffixed) and `stable` (minified) variants.

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
- [x] **Gradle Setup**: `gradle.properties` (JVM args), `libs.versions.toml` configured.
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
- [ ] **Experience Level**: Selection UI needed.
- [ ] **Equipment Setup**: Selection UI needed.

### Phase 4: Settings Feature (Partially Implemented)
- [x] **Theme Settings**: Functional UI for switching themes.
- [ ] **Unit Configuration**: Logic pending.
- [ ] **Backup & Restore**: UI placeholders added. Logic needed.
- [ ] **Developer Options**: UI placeholders added.
- [ ] **Updater**: Logic to check `dev` release notes using the specific version format is pending.

### Phase 5: Data Layer (Room) (Implemented)
- [x] **Entities**: `ExerciseEntity` created.
- [x] **Database**: `GymDatabase` configured with pre-population callback.
- [x] **DAO**: `ExerciseDao` implemented.
- [ ] **Migrations**: Logic for merging user vs built-in data needed later.

### Phase 6: Workout & Intelligent Input (Partially Implemented)
- [x] **Intelligent Text Box**: `PlateCalculator` logic implemented and unit-tested.
- [x] **UI Integration**: `WorkoutScreen` connects input to calculator.
- [x] **Exercise List**: `ExerciseListScreen` displays data from DB.
- [ ] **Workout Session**: Timers, Sets, Notifications not yet implemented.

### Phase 7: Profile & Stats (Placeholder)
- [x] **Basic Screen**: Placeholder UI.
- [ ] **Graphs**: Charts and stats logic needed.

## 7. Next Steps for Next Session
1.  **Migrate to Convention Plugins**: Refactor module build files to use `gymexe.*` plugins.
2.  **Updater Logic**: Implement the in-app updater in `feature:settings`.
3.  **Backup Implementation**: Implement the backup/restore logic (zipping DB + prefs to `.gym` file).
4.  **Workout Session**: Build the active workout logger (timers, set tracking).

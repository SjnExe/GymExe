# GymExe Project Plan

## 1. Project Summary
*   **App Name:** GymExe
*   **Package Name:** `com.sjn.gymexe`
*   **Repository:** `https://github.com/SjnExe/GymExe`
*   **Architecture:** MVVM with Clean Architecture (Data, Domain, Presentation layers).
*   **Tech Stack:**
    *   Language: Kotlin
    *   UI: Jetpack Compose (Material 3)
    *   DI: Hilt
    *   Database: Room (Offline-first)
    *   Async: Coroutines & Flow
    *   Build System: Gradle (Kotlin DSL)
*   **SDK Versions:** Min: 26 (Android 8.0), Target: 35 (Android 15).
*   **Theming:**
    *   Dark Mode: Pure Black (`#000000`)
    *   Light Mode: Material You (Dynamic Colors)
*   **Folder Structure:**
    *   Root should be clean.
    *   Development assets (Keystore, scripts) -> `Dev/`
    *   Documentation -> `Docs/`

## 2. CI/CD & Release Strategy
*   **Runner:** `ubuntu-24.04-arm` (ARM64).
*   **Workflow:** `build.yml` (Consolidated Pipeline)
    *   **Triggers:** Push to `dev`, Tag `v*`, Manual (`workflow_dispatch`).
    *   **Logic:**
        *   `dev` push -> Lints, Builds `beta`, Updates `beta` tag (Rolling Release).
        *   `v*` tag -> Builds `prod`, Creates GitHub Release.
*   **Signing:** Single `debug.keystore` used for BOTH Debug and Release to ensure signature consistency (allows upgrading Beta -> Stable).
*   **Update System:**
    *   Checks GitHub Releases.
    *   Beta tracks `beta` tag timestamps.
    *   Stable tracks Latest Release version name.
    *   ABI Detection: Downloads ARM64 specific APK if supported, else Universal.

## 3. Data & Backup Strategy
*   **Database:** Room.
    *   **Pre-population:** `assets/exercises.json` (50+ exercises).
    *   **Updates:** On app launch, merge `exercises.json` into DB. Update official exercises, preserve user custom exercises (flag `is_custom`).
    *   **Supersets:** Supported via flexible grouping (`group_id` / `order_in_workout`).
*   **Backup (Project Memory Protocol):**
    *   Format: `.gymexe` (Compressed ZIP containing DB JSON + Settings JSON).
    *   Storage: User picks folder ONCE (Scoped Storage permission persistence), then reuses.
    *   Compatibility: Universal (Forward/Backward compatible parsing).
    *   Intent Filter: Clicking `.gymexe` opens the app to restore.

## 4. Master Checklist

### Phase 1: Project Initialization & Structure
- [x] Initialize Gradle Project (Kotlin DSL, Version Catalogs).
- [x] Set up folder structure (`Dev/`, `Docs/`, `app/`).
- [x] Create `README.md`, `LICENSE`, `CODE_OF_CONDUCT.md`.
- [x] Generate `debug.keystore` and place in `Dev/`.
- [x] Configure `build.gradle.kts` (Signing Config, Flavors: `dev`, `prod`).
- [x] Configure GitHub Actions (`build.yml`).
- [x] **Update:** Fixed Gradle Wrapper (`gradlew`) and `.gitignore` to ensure clean build and repo hygiene.

### Phase 2: Core Architecture (Data & Domain)
- [x] Set up Hilt (Dependency Injection).
- [x] Set up Room Database.
    - [x] Define Entities: `Exercise`, `Workout`, `WorkoutExercise`, `Set`, `Log`.
    - [x] Create `assets/exercises.json`.
    - [x] Implement `ExerciseRepository` with "Merge Update" logic.
- [x] Implement `BackupManager`.
    - [x] JSON Export/Import logic.
    - [x] ZIP compression (`.gymexe`).
    - [x] Scoped Storage permission persistence logic.

### Phase 3: Domain Logic
- [x] Implement `UpdateManager` (GitHub API logic).
- [x] Implement Workout Scheduling Logic.
    - [x] Weekly vs Rolling Split.
    - [x] "Missed Workout" handling logic (Repository layer).
- [x] Implement `TimerManager` (Rest & Exercise timers).

### Phase 4: UI Implementation (Jetpack Compose)
- [x] Set up Theme (Pure Black Dark Mode, Dynamic Light Mode).
- [x] Create Vector Drawable Logo.
- [x] Implement Navigation (Compose Navigation).
- [x] **Screens:**
    - [x] Dashboard (Current Split status, Quick Start).
    - [x] Workout Player (Basic Logging, Timer Overlay).
    - [x] Exercise List (Search, Filter, Add Custom).
    - [x] Settings (Backup/Restore, Theme, Feedback Button, Update Check).

### Phase 5: Polishing & Validation
- [x] Add Feedback button (Link to GitHub Issues/Template).
- [x] Verify CI/CD pipeline (Workflows consolidated).
- [ ] Final Code Review & Lint check.

## 5. Context Log
*   **Initial Setup:** User requested specific folder structure (`Dev/`, `Docs/`) to keep root clean.
*   **Database Strategy:** Adopted "Option B" for exercise updates - merging JSON assets into DB on every update to keep official exercises fresh while preserving custom ones.
*   **Backup Format:** Decided on `.gymexe` (ZIP) for branding and efficiency.
*   **Build Issues:** Encountered missing Gradle Wrapper and polluted repo with `.gradle` cache files. Action taken: Generated wrapper, updated `.gitignore`, and cleaned cache.
*   **CI/CD Refactor:** Consolidated `beta_rolling` and `release_stable` into single `build.yml` for better maintenance. Trigger logic separates Beta (Branch push) and Stable (Tag push).

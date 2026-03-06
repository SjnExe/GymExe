# AGENTS.md

This file contains instructions for AI agents working on the **GymExe** repository.

## Core Directives for Agents
- **NO LOG REPORTS:** Do not generate file-based log reports (e.g., `error_log.txt`) or paste massive logs into the chat. Users view logs directly in their terminal by re-running commands.
- **Deep Planning:** Always engage in "Deep Planning Mode" before starting a task. Verify requirements through questions until absolute certainty is reached.

## Project Context
- **Name:** GymExe
- **Package:** `com.sjn.gym`
- **Purpose:** A modular, offline-first workout tracker for gym goers.
- **Tech Stack:**
  - Android (Kotlin 2.3.10 using Java 25)
  - Jetpack Compose (UI)
  - Hilt (Dependency Injection)
  - Room (Local Database)
  - DataStore (Preferences)
  - GitHub Actions (CI/CD)

## Coding Standards
- **Language:** Kotlin (Latest Stable)
- **UI:** Jetpack Compose (avoid XML layouts where possible)
- **Architecture:** Clean Architecture (MVVM/MVI)
  - `core/`: Shared modules (UI, Data, Model)
  - `feature/`: Feature specific modules (Workout, Settings, Onboarding)
  - `app/`: Entry point and navigation
- **Testing:**
  - Unit Tests for ViewModels and Domain logic.
  - UI Tests using Compose Test Rule.
- **Linting:** Strict mode enabled.
  - **Spotless:** Enforces ktfmt (kotlinlang style) formatting.

## Security
- **Signing:** Uses `app/debug.keystore` (tracked in git) with default credentials for simplicity.
- **Minification:** Always ensure Release builds are minified (`minifyEnabled true`).

## Build & Release
- **Flavors:**
  - `dev`: For development and PR checks (Debuggable, suffix `-dev`). Includes Dev Tools (Chucker, LeakCanary, File Logging).
  - `stable`: For production release (Minified).
- **Build Types:**
  - `release`: Minified, optimized, no test rules. Used for `devRelease` (manual QA) and `stableRelease` (Production).
  - `debug`: Non-minified, debuggable.
  - `benchmark`: Inherits from `release`. Minified but includes extra ProGuard rules to allow instrumented tests to run. Used for CI (`connectedDevBenchmarkAndroidTest`).
- **Versioning:** Automated via GitHub Actions.
  - Strategy: `v{Tag}-dev-PR{Num}.{Commits}`
  - Version Code: `Total Commits - 1`

## Environment
- **Local:** Standard Android Studio setup.
- **CI:** GitHub Actions (Ubuntu latest).

## Environment Setup

The project uses the **Foojay Toolchains Resolver**, which automatically downloads the required JDK (Java 25) during the build. No manual JDK installation is required.

To initialize the environment and run initial checks:
```bash
./gradlew aDD sC tDDUT --continue --no-daemon --build-cache --configuration-cache --parallel -q
```

## Useful Commands

### Pre-commit Verification
Run this before submitting any change:
```bash
./gradlew vCU sA tDDUT aDD -s --continue
```

### Building
*   **Build Debug APK (Dev):** `./gradlew aDD -s --continue`
*   **Build Release APK (Dev):** `./gradlew aDR -s --continue`
*   **Build Release APK (Stable):** `./gradlew aSR -s --continue`

### Quality & Testing
*   **Run Unit Tests:** `./gradlew tDDUT -s --continue`
*   **Format Code:** `./gradlew sA -s --continue`
*   **Record Roborazzi Baseline:** `./gradlew rRDD -s --continue`
*   **Verify Roborazzi Baseline:** `./gradlew vRDD -s --continue`
*   **Compare Roborazzi Baseline:** `./gradlew cRDD -s --continue`
*   **Generate HTML Test Coverage:** `./gradlew kHR -s --continue`
*   **Generate XML Test Coverage:** `./gradlew kXR -s --continue`

### Modularization
*   **Sync Project:** `./gradlew --refresh-dependencies`
*   **Clean Build:** `./gradlew clean build`

### Maintenance
*   **Update Dependencies:** `./gradlew vCU --no-configuration-cache`

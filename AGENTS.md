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
  - Android (Kotlin 2.3.10)
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
  - **Detekt:** `warningsAsErrors` is ON.
  - **Spotless:** Enforces Ktlint formatting.

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

The following script is already ran on Jules environment. This ensures all system dependencies are updated.

```bash
sudo apt-get update
sudo apt-get clean
sudo apt-get full-upgrade -y
sudo apt-get install -y openjdk-25-jdk
sudo update-java-alternatives --set java-1.25.0-openjdk-amd64
echo y | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;36.0.0"
sudo apt-get autoremove -y
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
./gradlew spotlessCheck lintDevDebug testDevDebugUnitTest assembleDevDebug --no-daemon --parallel --build-cache --stacktrace --warning-mode all --configuration-cache --continue
```

## Useful Commands

### Pre-commit Verification
Run this before submitting any change:
```bash
./gradlew versionCatalogUpdate spotlessApply lintDevDebug testDevDebugUnitTest assembleDevDebug --stacktrace --continue --parallel --build-cache
```

### Building
*   **Build Debug APK (Dev):** `./gradlew assembleDevDebug --stacktrace --continue --parallel --build-cache`
*   **Build Release APK (Dev):** `./gradlew assembleDevRelease --stacktrace --continue --parallel --build-cache`
*   **Build Release APK (Stable):** `./gradlew assembleStableRelease --stacktrace --continue --parallel --build-cache`

### Quality & Testing
*   **Run Lint:** `./gradlew lintDevDebug --stacktrace --continue --parallel --build-cache`
*   **Run Unit Tests:** `./gradlew testDevDebugUnitTest --stacktrace --continue --parallel --build-cache`
*   **Format Code:** `./gradlew spotlessApply --stacktrace --continue --parallel --build-cache`

### Modularization
*   **Sync Project:** `./gradlew --refresh-dependencies`
*   **Clean Build:** `./gradlew clean build`

### Maintenance
*   **Update Dependencies:** `./gradlew versionCatalogUpdate`

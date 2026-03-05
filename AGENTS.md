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
  - **Detekt:** `warningsAsErrors` is ON. Version 2.0.0-alpha.2 is used for Java 25 compatibility.
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

The following script is already ran on Jules environment. This ensures all system dependencies are updated.

```bash
sudo apt-get update -qq
sudo apt-get install -y -qq --no-install-recommends openjdk-25-jdk
sudo update-java-alternatives --set java-1.25.0-openjdk-amd64
yes | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;36.0.0" > /dev/null
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
./gradlew aDD --continue --no-daemon --build-cache --configuration-cache  --parallel -q
./gradlew sA tDDUT d --continue --no-daemon --build-cache --configuration-cache  --parallel -q
```

## Useful Commands

### Pre-commit Verification
Run this before submitting any change:
```bash
./gradlew vCU sA detekt tDDUT aDD -s --continue
```

### Building
*   **Build Debug APK (Dev):** `./gradlew assembleDevDebug -s --continue`
*   **Build Release APK (Dev):** `./gradlew assembleDevRelease -s --continue`
*   **Build Release APK (Stable):** `./gradlew assembleStableRelease -s --continue`

### Quality & Testing
*   **Run Lint:** `./gradlew d -s --continue`
*   **Run Unit Tests:** `./gradlew tDDUT -s --continue`
*   **Format Code:** `./gradlew spotlessApply -s --continue`
*   **Record Roborazzi Baseline:** `./gradlew rRDD -s --continue`
*   **Verify Roborazzi Baseline:** `./gradlew vRDD -s --continue`
*   **Compare Roborazzi Baseline:** `./gradlew cRDD -s --continue`
*   **Generate HTML Test Coverage:** `./gradlew kHR -s --continue`
*   **Generate XML Test Coverage:** `./gradlew kXR -s --continue`

### Modularization
*   **Sync Project:** `./gradlew --refresh-dependencies`
*   **Clean Build:** `./gradlew clean build`

### Maintenance
*   **Update Dependencies:** `./gradlew vCU`

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

The following script is already ran on Jules environment. This ensures all system dependencies are updated and the emulator is ready.

```bash
sudo apt update
sudo apt full-upgrade -y
sudo apt install -y openjdk-25-jdk
sudo update-java-alternatives --set java-1.25.0-openjdk-amd64
echo y | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;36.0.0"
sudo apt autoremove -y
sudo apt clean
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
if ! avdmanager list avd | grep -q "test"; then echo "no" | avdmanager create avd -n test -k "system-images;android-34;google_apis;x86_64" --force; fi
./gradlew :app:dependencies --configuration devBenchmarkRuntimeClasspath
```

## Useful Commands

### Quick Verification (Jules Default Check)
Run this before submitting any change:
```bash
./gradlew spotlessCheck lintDevDebug testDevDebugUnitTest
```

### Building
*   **Build Debug APK (Dev):** `./gradlew assembleDevDebug`
*   **Build Release APK (Dev):** `./gradlew assembleDevRelease`
*   **Build Release APK (Stable):** `./gradlew assembleStableRelease`

### Quality & Testing
*   **Run Lint:** `./gradlew lintDevDebug`
*   **Run Unit Tests:** `./gradlew testDevDebugUnitTest`
*   **Format Code:** `./gradlew spotlessApply`
*   **Run Instrumented Tests (CI/Agent):**
    1.  Start Emulator: `emulator -avd test -no-window -gpu swiftshader_indirect -noaudio &`
    2.  Wait for boot: `adb wait-for-device`
    3.  Run Tests: `./gradlew connectedDevBenchmarkAndroidTest`

### Modularization
*   **Sync Project:** `./gradlew --refresh-dependencies`
*   **Clean Build:** `./gradlew clean build`

### Maintenance
*   **Update Dependencies:** `./gradlew versionCatalogUpdate`

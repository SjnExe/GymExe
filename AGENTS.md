# AGENTS.md

This file contains instructions for AI agents working on the **GymExe** repository.

## Project Context
- **Name:** GymExe
- **Package:** `com.sjn.gym`
- **Purpose:** A modular, offline-first workout tracker for gym goers.
- **Tech Stack:**
  - Android (Kotlin)
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

## Security
- **Signing:** Uses `app/debug.keystore` (tracked in git) with default credentials for simplicity.
- **Minification:** Always ensure Release builds are minified (`minifyEnabled true`).

## Build & Release
- **Flavors:**
  - `dev`: For development and PR checks (Debuggable, suffix `-dev`).
  - `stable`: For production release (Minified).
- **Versioning:** Automated via GitHub Actions.
  - Strategy: `v{Tag}-dev-PR{Num}.{Commits}`
  - Version Code: `Total Commits - 1`

## Environment
- **Local:** Standard Android Studio setup.
- **CI:** GitHub Actions (Ubuntu latest).

## Environment Setup

The following script is already ran on Jules environment. This ensures all system dependencies are updated.

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
./gradlew --version
java -version
```

## Useful Commands

### Building
*   **Build Debug APK (Dev):** `./gradlew assembleDevDebug`
*   **Build Release APK (Dev):** `./gradlew assembleDevRelease`
*   **Build Release APK (Stable):** `./gradlew assembleStableRelease`

### Quality & Testing
*   **Run Lint:** `./gradlew lintDevDebug`
*   **Run Unit Tests:** `./gradlew testDevDebugUnitTest`
*   **Format Code:** `./gradlew spotlessApply` (if configured)

### Modularization
*   **Sync Project:** `./gradlew --refresh-dependencies`
*   **Clean Build:** `./gradlew clean build`

### Maintenance
*   **Update Dependencies:** `./gradlew versionCatalogUpdate`

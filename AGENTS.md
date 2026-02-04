# GymExe - Developer Agent Instructions

This repository contains the source code for the GymExe Android application.
Package Name: `com.sjn.gymexe`
Namespace: `com.sjn.gymexe`

## Environment Setup

The following script is already ran on Jules environment. This ensures all system dependencies are updated.

```bash
sudo apt autoremove -y
sudo apt clean
sudo apt update
sudo apt full-upgrade -y
```

## Build & Verify

*   **Build Debug**: `./gradlew assembleDebug`
*   **Run Tests**: `./gradlew test`
*   **Lint Check**: `./gradlew ktlintCheck detekt`

## Signing Configuration

For Release builds, the project uses a Keystore included in the repository.
*   **Keystore File**: `Dev/release.keystore`
*   **Credentials**: Hardcoded in `app/build.gradle.kts` (Alias: `gymexe`, Password: `gymexe`).

## Code Style

*   We use **KtLint** and **Detekt** for code style enforcement.
*   Run `./gradlew ktlintFormat` to auto-fix style issues.
*   Imports for `hiltViewModel` must use `androidx.hilt.lifecycle.viewmodel.compose` instead of `androidx.hilt.navigation.compose`.

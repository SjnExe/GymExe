# GymExe - Developer Agent Instructions

This repository contains the source code for the GymExe Android application.
Package Name: `com.sjn.gymexe`
Namespace: `com.sjn.gymexe`

## Environment Setup

To set up the development environment, run the following script. This ensures all system dependencies are updated and the project is ready to build.

```bash
# Cleans unnecessary packages
sudo apt autoremove -y
sudo apt clean
# Update system packages
sudo apt-get update && sudo apt-get upgrade -y

# Install Java 17 (Required for this project's Gradle configuration)
# Note: The environment might have Java 21, but we ensure compat with 17 as per build config.
sudo apt-get install -y openjdk-17-jdk-headless

# Ensure Gradlew is executable
chmod +x gradlew

# Initial Clean Build
./gradlew clean assembleDebug
```

## Build & Verify

*   **Build Debug**: `./gradlew assembleDebug`
*   **Run Tests**: `./gradlew test`
*   **Lint Check**: `./gradlew ktlintCheck detekt`

## Signing Configuration

For Release builds, the project uses a Keystore.
*   **Local Development**: Uses `Dev/debug.keystore` (insecure, for dev only).
*   **CI/CD / Production**: Should use Environment Variables to inject the Keystore securely.
    *   `KEYSTORE_BASE64`: Base64 encoded content of the keystore file.
    *   `KEY_ALIAS`: Key alias.
    *   `KEY_PASSWORD`: Key password.
    *   `KEYSTORE_PASSWORD`: Keystore password.

## Code Style

*   We use **KtLint** and **Detekt** for code style enforcement.
*   Run `./gradlew ktlintFormat` to auto-fix style issues.
*   Imports for `hiltViewModel` must use `androidx.hilt.lifecycle.viewmodel.compose` instead of `androidx.hilt.navigation.compose`.

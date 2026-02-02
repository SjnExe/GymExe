# GymExe

**GymExe** is an open-source, offline-first gym tracker built for Android using Jetpack Compose and Modern Android Architecture. It is designed to be a high-quality, privacy-focused alternative to commercial apps.

## Features
*   **Offline First:** All data is stored locally using Room Database.
*   **Modern UI:** Built with Jetpack Compose and Material 3 (Pure Black Dark Mode).
*   **Advanced Tracking:** Supports Supersets, Drop Sets, and custom exercises.
*   **Smart Scheduling:** Weekly or Rolling Split options.
*   **Data Freedom:** Backup and Restore to JSON/ZIP (`.gymexe`).

## Architecture
*   **Language:** Kotlin
*   **Pattern:** MVVM + Clean Architecture
*   **DI:** Hilt
*   **Database:** Room

## Build & CI/CD
*   **CI:** GitHub Actions (Ubuntu ARM64 runners).
*   **Flavors:** `dev` (Beta/Nightly) and `prod` (Stable).
*   **Signing:** Consistent signing key for seamless updates from Beta to Stable.

## Contributing
See [Docs/](Docs/) for more information.

## License
MIT License.

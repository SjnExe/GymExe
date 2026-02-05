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

## 2. Roadmap

### Phase 1: UI Implementation & UX Refinement
*   **Goal:** Move from "basic/bad UI" to a polished, professional Material 3 interface.
*   **Tasks:**
    - [ ] **Design Refinement:** Define typography, shapes, and color palettes (Pure Black).
    - [ ] **Dashboard:** Implement a rich dashboard showing active split, streaks, and quick actions.
    - [ ] **Workout Player:** Build a robust workout logging UI with sets, reps, weight, and rest timer overlay.
    - [ ] **Exercise List:** Create a searchable, filterable list with animations and custom exercise support.
    - [ ] **Settings:** Polish the settings screen (Backup/Restore, Theme toggles, About).

### Phase 2: Domain Logic & Features
*   **Goal:** Implement the core "gym" logic.
    - [ ] **Update Manager:** Implement in-app updates checking GitHub Releases.
    - [ ] **Workout Scheduling:** Implement Weekly vs Rolling Split logic.
    - [ ] **Timers:** Implement reliable Rest & Exercise timers (Foreground Services if needed).
    - [ ] **History & Analytics:** Visualize workout history and progress.

### Phase 3: Polishing & Validation
- [ ] Add Feedback button (Link to GitHub Issues).
- [ ] Final Code Review & Lint check.
- [ ] Comprehensive UI Tests.

## 3. Infrastructure (Completed/Maintained)
- [x] **CI/CD:** GitHub Actions pipeline (`quality_check`, `build_beta`, `release_stable`) on `ubuntu-latest`.
- [x] **Versioning:** Dynamic semantic versioning based on Git tags and commits.
- [x] **Code Quality:** KtLint and Detekt integrated.
- [x] **Database:** Room setup with asset pre-population (`exercises.json`).
- [x] **Backup:** `.gymexe` import/export system.

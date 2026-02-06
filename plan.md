# GymExe Project Blueprint & Handover

## 🚨 Handover Status (Current State)
*   **Active Branch:** `feat/apk-splits-settings-ui-refactor` (or similar).
*   **CI Status:** **Failing Build.**
    *   **Error:** `Unresolved reference 'hiltViewModel'` in `SettingsScreen.kt`.
    *   **Cause:** The `hiltViewModel()` Composable function requires the `androidx.hilt:hilt-navigation-compose` dependency, which is currently missing from `libs.versions.toml` and `app/build.gradle.kts`.
    *   **Immediate Action Required:** Add `androidx.hilt:hilt-navigation-compose` to dependencies to fix the build.
*   **Implemented:**
    *   **CI/CD:** `build.yml` is refactored for `ubuntu-latest`, Split APKs, and Rolling Beta releases.
    *   **UI Skeleton:** `Navigation.kt` (Bottom Bar + Top Settings), `Theme.kt` (Pure Black/Dynamic), `SettingsScreen.kt` (UI Layout).
    *   **Preferences:** `UserPreferencesRepository` (DataStore) and `SettingsViewModel` are implemented.
*   **Pending (Phase 1):**
    *   Fix the build error.
    *   Connect `SettingsScreen` UI to `SettingsViewModel` logic (currently placeholders).
    *   Implement "Check for Updates" logic (GitHub API).
    *   Implement "Copy Logs" logic.

---

## 1. Vision & Architecture
**GymExe** is an offline-first, open-source workout tracker for Android (Kotlin, Compose, Hilt, Room). Focus: Efficiency, Data Depth, Power User features.

*   **Package:** `com.sjn.gymexe`
*   **Min/Target SDK:** 26 / 35.
*   **Theme:** Material You + System Default. **Dark Mode:** Pure Black (`#000000`).

## 2. Core Features (Blueprint)

### 2.1 Update System
*   **Source:** GitHub Releases.
*   **Trigger:** On App Open (Foreground, 4h interval).
*   **Logic:** Beta checks `beta` tag. Stable checks `latest`.
*   **UI:** Badge -> Download -> Install Intent.

### 2.2 Data & Storage
*   **Units:** Store as **SI (Metric)**. Display as User Preference (Smart Rounding 2 decimals).
*   **Backup:** `.gymexe` (ZIP of JSONs).
*   **Exercise DB:** Pre-populated `assets/exercises.json`. Versioned merging (preserve `is_custom=true`).

### 2.3 Workout Engine
*   **Math Input:** `20+5` -> `25`. Space=`+`. `*`/`x`=`Multiply`. PEMDAS.
*   **Log Types:** Standard, Assisted (Negative Weight), Weighted Bodyweight, Duration, Cardio.
*   **Smart Prefill:** Heatmap colors (Light->Heavy). Logic: Most Used / Last Used.
*   **Timers:** Actual vs Target rest tracking.

### 2.4 Domain Logic
*   **Stats:** Height (Profile), Weight (Daily).
*   **Charts:** "This Week" vs "Last Week" overlay. Handle missing data (gaps) gracefully.
*   **Emergency Rest:** Flagging days.

## 3. UI Structure
1.  **Dashboard:** Split status, Streak.
2.  **Workout:** Active Session, Input Rows, Timer Overlay.
3.  **Exercises:** Library, Search, Merge Tools.
4.  **Profile (You):** History, Stats, Charts.
5.  **Settings (Top Bar):** Theme, Units, Backup, Update, Debug Logs.

## 4. Roadmap

### Phase 1: Foundation (In Progress)
- [x] **CI/CD:** Split APKs, Dynamic Versioning (`v{Tag}-beta-PR{Num}`), Rolling Beta.
- [x] **Skeleton:** Navigation, Theme, Settings UI.
- [ ] **Fix:** `hilt-navigation-compose` dependency.
- [ ] **Feature:** Settings Logic (Theme/Unit toggle persistence).

### Phase 2: The Workout Engine
- [ ] **Database:** Room Entities (Exercise, Workout, Set).
- [ ] **Exercise Repo:** JSON Loader & Merge.
- [ ] **Active Workout UI:** Math Input, Timer Service.

### Phase 3: Intelligence & Polish
- [ ] **Smart Prefill:** Algorithms.
- [ ] **History:** Charts & Calendars.
- [ ] **Release:** Beta Tagging loop verification.

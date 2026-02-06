# GymExe Project Blueprint & Handover

## 🚨 Handover Status (Current State)
*   **CI Status:** **Failing Build.**
    *   **Error:** `Unresolved reference 'hiltViewModel'` in `SettingsScreen.kt`.
    *   **Cause:** The `hiltViewModel()` Composable function requires the `androidx.hilt:hilt-navigation-compose` dependency, which is currently missing from `libs.versions.toml` and `app/build.gradle.kts`.
    *   **Immediate Action Required:** Add `androidx.hilt:hilt-navigation-compose` to dependencies to fix the build.
*   **Implemented:**
    *   **CI/CD:** `build.yml` is refactored for `ubuntu-latest`, Split APKs (`arm64`, `universal`), and Rolling Beta releases.
    *   **UI Skeleton:** `Navigation.kt` (Bottom Bar + Top Settings), `Theme.kt` (Pure Black/Dynamic), `SettingsScreen.kt`.
    *   **Preferences:** `UserPreferencesRepository` (DataStore) implemented.

---

## 1. Vision & Architecture
**GymExe** is an offline-first, open-source workout tracker for Android. It prioritizes efficiency, deep data logging, and "Power User" features.

*   **Package:** `com.sjn.gymexe`
*   **Min/Target SDK:** 26 / 35.
*   **Architecture:** MVVM + Clean Architecture + Hilt + Room.
*   **Theming:**
    *   **Default:** Material You + System Mode (Follows OS).
    *   **Dark Mode:** Option for **Pure Black** (`#000000`) for OLED efficiency.
    *   **Light Mode:** Material You / Brand Colors.

## 2. Core Features (Detailed)

### 2.1 Update System
*   **Source:** GitHub Releases API.
*   **Trigger:** **On App Open** (Foreground Check). Interval: Once every **4 hours**.
*   **Channels:**
    *   **Stable:** Checks `latest` release tag.
    *   **Beta:** Checks `beta` tag (Rolling Release).
*   **UI Flow:** Circular progress badge -> Download to internal storage -> Prompt Install Intent.

### 2.2 Data & Storage
*   **Offline First:** Internet used *only* for Updates and optional Video Tutorials.
*   **Units Strategy:**
    *   **Storage:** Always **SI Units** (Metric - kg, meters) in DB.
    *   **Display:** Converted to User Preference (lbs, miles) on the fly.
    *   **Rounding:** Smart rounding to **2 decimal places** to avoid "10.999 lbs" artifacts. User input should be preserved in UI state where possible.
*   **Backup:** `.gymexe` format (ZIP containing JSON dumps). Forward/Backward compatible.
*   **Exercise Database:**
    *   Pre-populated from `assets/exercises.json`.
    *   **Versioning:** `exercises.json` has a version field. App launch merges new official exercises without overwriting user customizations.
    *   **Custom Exercises:** Flagged `is_custom=true`.
    *   **Merge Tool:** If an official update adds an exercise a user already created (e.g., "My Plank" vs Official "Plank"), provide a tool to **merge logs** into the official one.

### 2.3 Workout Logging Engine (The Core)
*   **Math Input Field:**
    *   **Implicit Addition:** Space acts as `+` (e.g., `5 5 2.5` = `12.5`).
    *   **Multiplication:** Support `*`, `x`, `×`.
    *   **Order of Operations:** Standard PEMDAS (Multiply before Add).
    *   **Example:** `4*5 2*2.5` -> `(4*5) + (2*2.5)` -> `20 + 5` -> `25`.
*   **Log Types (Schema):**
    *   `Standard`: Weight + Reps.
    *   `Assisted`: **Negative Weight**. Logic: Lower weight = Harder/Heavier effort.
    *   `Weighted Bodyweight`: Bodyweight + Added Weight.
    *   `Duration`: Time (e.g., Plank). Support "Weighted Plank".
    *   `Cardio`: Flexible fields (Distance + Time, Speed + Time, Distance only).
*   **Smart Prefill:**
    *   **Logic:** Suggest weights based on "Most Used", "Last Used", or "PR".
    *   **UI:** Heatmap-style colored chips (Light = Warmup, Red/Dark = Heavy).
    *   **Assisted Color Logic:** Reversed (Light = High Assist/Easy, Red = Low Assist/Hard).
*   **Timers:**
    *   **Target Rest:** User sets (e.g., 90s).
    *   **Actual Rest:** Auto-logged time between finishing Set A and starting Set B.
    *   **Notification:** Dismissible "Rest Timer" notification with Actions (+30s, Skip). Tapping opens App.

### 2.4 Domain Logic & Stats
*   **Daily Log:** Body Weight (Morning/Night tags).
*   **Profile:** Height (Static).
*   **Analytics & Charts:**
    *   **Comparisons:** "This Week" vs "Last Week" overlay graphs.
    *   **Missing Data:** Handle gaps gracefully (broken lines or gaps). **Do NOT** interpolate fake zeros.
    *   **Emergency Rest:** Explicit "Rest Day" or "Emergency" flags for days with no logs.

## 3. UI Structure (Material 3)
1.  **Dashboard (Home):** Active Split Status, Streak, Quick Actions.
2.  **Workout (Player):** Active Session List, Math Input Rows, Timer Overlay.
3.  **Exercises (Library):** Searchable List, Filters (Muscle, Equipment), Merge Tools.
4.  **Profile ("You"):** History (Calendar/List), Stats (Body Weight, Height), Charts.
5.  **Settings (Top Bar Action):**
    *   **Theme:** Segmented Button (System/Light/Dark) + Material You Toggle.
    *   **Units:** Toggle.
    *   **Data:** Backup/Restore, Copy Debug Logs.
    *   **Update:** Check for Update.

## 4. Roadmap

### Phase 1: Foundation (In Progress)
- [x] **CI/CD:** Split APKs, Dynamic Versioning, Rolling Beta.
- [x] **Skeleton:** Navigation, Theme, Settings UI.
- [ ] **Fix:** Add `hilt-navigation-compose` dependency.
- [ ] **Feature:** Finish Settings Logic (Theme/Unit toggle persistence).

### Phase 2: The Workout Engine
- [ ] **Database:** Define Room Entities (Exercise, Workout, Set, Log).
- [ ] **Exercise Repo:** Implement JSON Loader & Versioned Merge Logic.
- [ ] **Active Workout UI:** Implement Input rows with Math Parser.

### Phase 3: Intelligence & Polish
- [ ] **Smart Prefill:** Implement "Most Used" algorithms & Heatmap UI.
- [ ] **History:** Implement Charts & Calendars (handling sparse data).
- [ ] **Release:** Beta Tagging & Feedback loop.

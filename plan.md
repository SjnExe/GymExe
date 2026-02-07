# GymExe Project Blueprint & Handover

## 🚨 Handover Status (Current State)
*   **CI Status:** **Passing.**
    *   **Fixed:** `kspDevDebugKotlin` build failure resolved by updating DAOs to explicit `suspend` return types and adding `@JvmSuppressWildcards`.
    *   **Fixed:** `icons` resolution error resolved by adding `androidx.compose.material:material-icons-extended`.
*   **Implemented:**
    *   **CI/CD:** `build.yml` updated for AGP 9.0, Artifact sorting, and Versioning fixes.
    *   **Dependencies:** Updated to latest stable: AGP 9.0.0, Kotlin 2.3.10, Hilt 2.59.1, Compose 2026.01.01.
    *   **Dev Tools:** Added LeakCanary, Timber (FileLoggingTree), StrictMode.
    *   **UI:** Settings moved to Profile, Units UI refactored (Weight/Distance segments).
    *   **Logic:** `ExerciseRepository` (JSON sync), `MathInputParser` (exp4j).

---

## 1. Vision & Architecture
**GymExe** is an offline-first, open-source workout tracker for Android (Kotlin, Compose, Hilt, Room). Focus: Efficiency, Data Depth, Power User features.

*   **Package:** `com.sjn.gymexe`
*   **Min/Target SDK:** 26 / 36.
*   **Architecture:** MVVM + Clean Architecture + Hilt + Room.
*   **Theming:**
    *   **Default:** Material You + System Mode (Follows OS).
    *   **Dark Mode:** Option for **Pure Black** (`#000000`) for OLED efficiency.
    *   **Light Mode:** Material You / Brand Colors.
    *   **Edge-to-Edge:** Must be fully enabled with correct `WindowInsets` handling (status bar/navigation bar).

## 2. Core Features (Detailed)

### 2.1 Update System
*   **Source:** GitHub Releases API.
*   **Trigger:**
    *   **Automatic:** On App Open (Foreground Check). Interval: Once every **4 hours**.
    *   **Manual:** "Check for Update" button in Settings. **Must provide immediate feedback** (Downloading / No Update Found Toast).
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
    *   **Update:** Check for Update (with explicit feedback).

### 3.1 Adaptive UI Strategy
*   **Core Principle:** UI must adapt to Window Size Class and Aspect Ratio (Split Screen/Floating Window).
*   **Floating Window:** Optimized for compact/small windows. Must handle overlap gracefully.
*   **Split Screen:** Detect variants (Top/Bottom vs Left/Right) and adjust layout density/navigation.
*   **Navigation Modes:**
    *   **Standard (Portrait):** Bottom Navigation Bar.
    *   **Wide / Landscape (Split Top-Bottom):** Navigation Rail (Left) or Drawer.
    *   **Floating (Small):** Compact Bottom Bar or Drawer.

## 4. Roadmap

### Phase 1: Foundation (In Progress)
- [x] **CI/CD:** Split APKs, Dynamic Versioning, Rolling Beta.
- [x] **Skeleton:** Navigation, Theme, Settings UI.
- [x] **Fix:** Add `hilt-navigation-compose` dependency to `libs.versions.toml` and `app/build.gradle.kts`.
- [x] **Feature:** Finish Settings Logic (Theme/Unit toggle persistence).
- [x] **UI:** Refactor Settings navigation (Move to Profile, fix back stack).
- [x] **CI/CD:** Fix Version Name duplication and Artifact sorting.
- [x] **Dev:** Add Timber, LeakCanary, Dependency Analysis.
- [x] **Fix:** KSP2 Build Failure & Missing Icons.

### Phase 2: The Workout Engine
- [ ] **UI:** Fix Edge-to-Edge overlaps (Status/Nav bars).
- [ ] **Feature:** Implement Update Check Feedback (Toast/Dialog).
- [ ] **UI:** Implement Adaptive Layouts (Floating/Split Screen logic).
- [x] **Database:** Define Room Entities (Exercise, Workout, Set, Log).
- [x] **Exercise Repo:** Implement JSON Loader & Versioned Merge Logic.
- [ ] **Active Workout UI:** Implement Input rows with Math Parser.

### Phase 3: Intelligence & Polish
- [ ] **Smart Prefill:** Implement "Most Used" algorithms & Heatmap UI.
- [ ] **History:** Implement Charts & Calendars (handling sparse data).
- [ ] **Release:** Beta Tagging & Feedback loop.

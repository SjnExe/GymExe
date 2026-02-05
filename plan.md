# GymExe Project Blueprint

## 1. Vision & Architecture
**GymExe** is an offline-first, open-source workout tracker for Android, built with modern tech (Kotlin, Compose, Hilt, Room). It focuses on efficiency, deep data logging, and "Power User" features like math-input fields and intelligent pre-fills.

*   **Package Name:** `com.sjn.gymexe`
*   **Min SDK:** 26 (Android 8.0) | **Target SDK:** 35
*   **Architecture:** MVVM + Clean Architecture (Data -> Domain -> UI).
*   **Theming:**
    *   **Default:** Material You + System Mode (Follows OS Light/Dark).
    *   **Dark Mode:** Option for Pure Black (`#000000`) for OLED efficiency.
    *   **Light Mode:** Material You / Brand Colors.
*   **Localization:** System Default (English fallback).

## 2. Core Features

### 2.1 Update System
*   **Source:** GitHub Releases API.
*   **Channels:**
    *   **Stable:** Checks `latest` release tag.
    *   **Beta:** Checks `beta` tag (Rolling Release).
*   **Trigger:** On App Open (Foreground Check). Interval: Once every 4 hours.
*   **UI:** "Update Available" Badge/Prompt -> Download -> Install.

### 2.2 Data & Storage (Room DB)
*   **Offline First:** All data stored locally. Internet used only for Updates/Tutorials.
*   **Units:** Stored as **SI Units** (Metric - kg, meters) in DB. Displayed in User Preference (lbs, miles) with smart rounding (2 decimal places). **Default: SI.**
*   **Missing Data:** No placeholders (e.g., 0 height). UI handles missing data gracefully.
*   **Backup:** `.gymexe` (ZIP containing JSONs).
*   **Exercises Database:**
    *   Pre-populated from `assets/exercises.json`.
    *   **Versioning:** `exercises.json` has a version. App launch merges updates (adds new exercises/fields) without overwriting user customizations.
    *   **Custom Exercises:** Flagged `is_custom=true`. Mergeable with Official if names collide.

### 2.3 Workout Logging Engine
*   **Input Handling (Power User Math):**
    *   **Operators:** `+` (Addition), `*` / `x` / `×` (Multiplication).
    *   **Implicit Addition:** Space acts as `+`.
    *   **Logic:** Standard Order of Operations (Multiply then Add).
    *   **Examples:**
        *   `20+7.5` -> `27.5`
        *   `5 5 12.5` -> `22.5`
        *   `4*5 2*2.5` -> `20 + 5` -> `25`
*   **Log Types:**
    *   `Standard`: Weight + Reps.
    *   `Assisted`: Negative Weight (Lower = Harder).
    *   `Weighted Bodyweight`, `Duration` (Time), `Cardio` (Dist/Time/Speed - flexible).
*   **Smart Prefill:** "Most Used", "Last Used", "PR". Heatmap UI.
*   **Timers:** Target Rest vs Actual Rest (Auto-logged).

### 2.4 Domain Logic: Body Stats & Progress
*   **Daily Log:** Body Weight (Morning/Night tags).
*   **Stats:** Height (Profile).
*   **Analytics:**
    *   **Comparisons:** "This Week" vs "Last Week" overlays.
    *   **Handling Missing Data:** Partial weeks/months shown accurately. No interpolation of fake zeros.
    *   **Emergency Rest:** Support for flagging forced rest days.

## 3. UI/UX Structure (Material 3)

### 3.1 Navigation (Bottom Bar)
1.  **Dashboard (Home):**
    *   Active Split Status (e.g., "Push Day").
    *   Quick Actions: "Start Scheduled", "Quick Log".
    *   Weekly Streak / Consistency Graph.
2.  **Workout (Player):**
    *   **Active Session UI:** List of exercises in current workout.
    *   **Exercise Card:** Set List, Math Input Rows, Timer Overlay.
3.  **Exercises (Library):**
    *   Searchable, Filterable (Muscle, Equipment).
    *   **Detail View:** Instructions, History, Merge Tools.
4.  **History (Logs):**
    *   Calendar View / List View.
5.  **Settings:**
    *   **Theme:** Dark/Light/System + Material You Toggle.
    *   **Units:** "Display Units" Selector.
    *   **Backup:** Export/Import/Schedule.
    *   **Update:** Check for Update (Badge).
    *   **Debug:** "Copy Error Logs" (Errors/Warnings only).

## 4. Roadmap

### Phase 1: Foundation & UI Skeleton
- [ ] **Scaffold:** Bottom Navigation, Theme Setup (Material You + Pure Black).
- [ ] **Settings:** Theme Toggle, Unit Logic, Update Checker UI.
- [ ] **Database:** Room Entities (Exercise, Workout, Set).
- [ ] **Exercise Repo:** JSON Asset Loader & Merge Logic.

### Phase 2: The Workout Engine
- [ ] **Exercise List:** Search, Filter, Add Custom.
- [ ] **Active Workout UI:** Input rows, Math Evaluator, Timer Service.
- [ ] **Logging Logic:** Save sets, Handle Metric/Imperial conversion.

### Phase 3: Intelligence & Polish
- [ ] **Smart Prefill:** "Most Used" algorithms & Heatmap UI.
- [ ] **History:** Calendar & Charts (Missing Data handling).
- [ ] **Release:** Beta Tagging & Feedback loop.

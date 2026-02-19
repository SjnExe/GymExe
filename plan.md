# GymExe Development Plan

## 1. Project Overview
**App Name:** GymExe
**Package Name:** com.sjn.gym
**Purpose:** A modular, offline-first workout tracker for gym goers.
**Target OS:** Android (Min SDK 26, Target SDK 36).
**Philosophy:** Modular architecture, latest stable tools, absolute control over data (backups, logs), and "intelligent" input methods.

## 2. Environment & Tooling
*   **Language:** Kotlin
*   **JDK:** Java 25 (OpenJDK)
*   **Build System:** Gradle 9.3.1
*   **Android SDK:** Compile 36, Min 26.
*   **UI Framework:** Jetpack Compose (Material 3).
*   **Dependency Injection:** Hilt.
*   **Database:** Room (SQLite).
*   **Network:** Retrofit/OkHttp (for updater only).
*   **Testing:** Roborazzi (Screenshots), JUnit, Kover (Coverage).

## 3. Architecture
The app will follow a modular architecture:
*   `:app`: Main entry point, navigation graph.
*   `:core:ui`: Design system, themes, common components.
*   `:core:data`: Database, Repository implementations, DataStore.
*   `:core:model`: Domain entities (Workout, Set, Profile).
*   `:feature:settings`: Settings screens (Theme, Units, Backup, Developer).
*   `:feature:workout`: Workout logging, Intelligent Text Box, Exercise list.
*   `:feature:profile`: "You" page, stats, graphs.
*   `:feature:onboarding`: Setup wizard.

## 4. Development Workflow & CI/CD
**Branching Strategy:**
*   `main`: Stable releases.
*   `dev`: Active development (default branch).

**CI/CD (GitHub Actions):**
*   **File:** `.github/workflows/build.yml`
*   **Triggers:**
    *   `workflow_dispatch`: Manual dev builds.
    *   `pull_request`: Checks PRs against `dev`.
    *   `push` (tags `v*`): Stable releases.
*   **Versioning:**
    *   **Stable:** From tag (e.g., `v1.2.3` -> `1.2.3`).
    *   **Dev:** `{latest_stable}-dev-{pr_number}.{commit_count}` or `{latest_stable}-dev-run{run_number}`.
*   **Artifacts:**
    *   `stable`: Universal APK + Splits (R8 enabled, no debug).
    *   `dev`: `GymExe-release.apk` (R8 enabled, debuggable) + `GymExe-debug.apk` (No R8).

## 5. Implementation Checklist

### Phase 1: Repository & Environment Setup
- [ ] **Initialize Repository**: Create `.gitignore`, `README.md`, `LICENSE` (MIT).
- [ ] **AGENTS.md**: Create file with the specific environment setup script provided.
- [ ] **Gradle Setup**:
    - [ ] Configure `gradle/libs.versions.toml` with all specified versions.
    - [ ] Set up `build-logic` plugins (Convention Plugins).
    - [ ] Configure `gradle.properties` (JVM args, caching).
- [ ] **Signing Setup**:
    - [ ] Create script to generate/retrieve keystore from GitHub Secrets.
    - [ ] Store keystore in `GymExe/signature` (in CI environment).
- [ ] **CI/CD Pipeline**:
    - [ ] Implement `build.yml` with the logic for versioning, caching, and multi-flavor building.
    - [ ] Create the log aggregation script.

### Phase 2: Core Architecture & UI Foundation
- [ ] **Icons**: Create Vector Assets (Adaptive Icon, Notification Icon).
- [ ] **Theming Engine**:
    - [ ] Implement Material 3 Theme.
    - [ ] Support System/Dark/Light modes.
    - [ ] Support Dynamic Colors (Material You) with fallback to custom presets.
    - [ ] Save theme preference in DataStore.
- [ ] **Navigation**: Set up Compose Navigation with Type-safe arguments.
- [ ] **Permissions**: Handle Runtime Permissions (Notifications, etc.).

### Phase 3: Onboarding (Setup Wizard)
- [ ] **Welcome Screen**: "New User" vs "Restore Backup".
- [ ] **Profile Setup**:
    - [ ] Gender, Height, Weight, Date of Birth.
    - [ ] Units preference (Metric/Imperial).
- [ ] **Experience Level**:
    - [ ] Free style (No fixed pattern).
    - [ ] Fixed Day/Week (e.g., Monday = Chest).
    - [ ] Splits (Pattern based, e.g., PPL).
- [ ] **Equipment Setup**:
    - [ ] "None", "Full Gym", or "Specific".
    - [ ] Prefill weight defaults based on equipment.

### Phase 4: Settings Feature
- [ ] **Unit Configuration**:
    - [ ] Weight: kg/lbs.
    - [ ] Distance: km/mi.
    - [ ] Measurement: cm/in, m/ft.
    - [ ] Logic to store in SI, display in User Pref.
- [ ] **Theme Settings**:
    - [ ] Mode (System/Dark/Light).
    - [ ] Color (Dynamic/Preset/Custom).
- [ ] **Backup & Restore**:
    - [ ] Path: `Documents/GymExe`.
    - [ ] Extension: `.gym` (Compressed JSON/Protobuf).
    - [ ] Manual Backup & Restore UI.
    - [ ] Auto-backup frequency & limit (delete old).
- [ ] **Developer Options**:
    - [ ] Copy Logs / Save Logs.
    - [ ] Visible only in `dev` flavor or via hidden trigger.
- [ ] **Updater**:
    - [ ] UI to check for updates.
    - [ ] Auto-check interval (User preset).
    - [ ] Flavor awareness (Dev checks dev tags, Stable checks stable tags).
- [ ] **First Day of Week**:
    - [ ] Auto-detect from Calendar or default to Sunday.
    - [ ] Manual override.

### Phase 5: Data Layer (Room)
- [ ] **Entities**:
    - [ ] `Exercise` (Name, Body Part, Muscle, Type).
    - [ ] `Workout` (Name, Description, Type).
    - [ ] `WorkoutLog` (Date, Time, Duration).
    - [ ] `SetLog` (Weight, Reps, RPE, Type).
- [ ] **Pre-population**:
    - [ ] Create a robust database of "Famous Workouts" and Exercises.
    - [ ] Ensure DB migrations handle user-added content vs. built-in updates (merging logic).

### Phase 6: Workout & Intelligent Input
- [ ] **Intelligent Text Box (Plate Calculator)**:
    - [ ] Input: `7.5 3x12.5` -> Parses as "1x 7.5kg plate" + "3x 12.5kg plates".
    - [ ] Operators: Space/+ (Add), x/*/× (Multiply quantity).
    - [ ] Syntax Highlighting: Colors for Quantity, Weight, Operator.
    - [ ] Prefill Buttons: Dynamic row based on Equipment type.
- [ ] **Exercise List**:
    - [ ] Filter by Body Part, Muscle, Equipment.
    - [ ] Sort by Popularity/Usage.
    - [ ] Favorites (Star).
    - [ ] Grid/List view toggle.
- [ ] **Workout Session**:
    - [ ] Set Types: Warmup, Failure, Drop set, Negative, etc.
    - [ ] Timers: Rest timer (auto/manual), Stopwatch.
    - [ ] Floating Window support (PiP).
    - [ ] Notification controls ("Complete Set").

### Phase 7: Profile & Stats ("You" Page)
- [ ] **Personal Info**: Display/Edit details.
- [ ] **Graphs**: Progress charts (Weight lifted, Reps, Consistency).
- [ ] **History**: Calendar view of workouts.

## 6. Logic Specifications

### Intelligent Text Box Parsing Rule
*   **Context**: Used for plate-loaded exercises.
*   **String**: `A BxC D`
*   **Tokenization**: Split by space (ignoring extra spaces).
*   **Parsing**:
    *   If token is number `N`: Add `1` count of weight `N`.
    *   If token is `QxW`: Add `Q` count of weight `W`.
    *   Accumulate total weight based on parsed plates + Base Bar weight (configurable).

### Versioning Script Logic
```bash
# (Pseudo-code for CI)
if [[ $GITHUB_REF == refs/tags/v* ]]; then
  VERSION_NAME=${GITHUB_REF#refs/tags/v}
  VERSION_CODE=$(git rev-list --count HEAD)
else
  # Dev logic
  LATEST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "0.0.0")
  COMMITS_SINCE=$(git rev-list --count HEAD ^$LATEST_TAG)
  VERSION_NAME="${LATEST_TAG}-dev-${GITHUB_RUN_NUMBER}"
  VERSION_CODE=$(( $(git rev-list --count HEAD) ))
fi
```

### Backup (.gym) Spec
*   **Format**: ZIP archive.
*   **Contents**:
    *   `database.db` (SQLite dump or JSON export).
    *   `settings.json` (Preferences).
    *   `metadata.json` (Version, Date, Device).
*   **Compatibility**: Always include schema version to allow migration on restore.

## 7. Next Steps for Jules (Session Handover)
1.  Read this `plan.md` carefully.
2.  Start with **Phase 1**.
3.  **Do not** deviate from the "Java 25" requirement unless the environment setup explicitly fails, then report back.
4.  Ensure `AGENTS.md` is created exactly as requested.
5.  Wait for user confirmation before pushing the initial repo structure.

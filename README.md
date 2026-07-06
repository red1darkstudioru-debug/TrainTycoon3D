# TrainTycoon3D

Android/Kotlin/libGDX 3D starter engine for a railway tycoon.

## Features

- Android Studio project with Gradle Kotlin DSL.
- libGDX 3D scene with terrain, rails, station placeholders, lighting and RTS-style camera controls.
- Automatic asset registry scans `app/src/main/assets/models`, `textures`, `sounds`, and `configs` at startup.
- `.glb`, `.png`, `.wav`, and `.json` assets are registered without hardcoded model lists.
- Modular packages for engine, world/grid, railway, trains, buildings, economy, save, and UI.
- Scene2D HUD with Build, Train, Delete, and Pause controls.

## Run

Open the repository in Android Studio and press **Run** on the `app` configuration. Gradle downloads Android, Kotlin, libGDX, Gson, and DataStore dependencies automatically.

## CLI check

```bash
gradle :app:assembleDebug
```

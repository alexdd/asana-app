# Yoga Asana Timer (Android, Jetpack Compose)

Eine moderne Android-App zur Verwaltung und Ausführung von Yoga-Sequenzen. Die ursprüngliche Kivy/Buildozer-Basis wurde vollständig durch eine native Kotlin-App ersetzt, die sich lokal zuverlässig als APK bauen lässt.

## Features

- Sequenzen mit beliebig vielen Asanas erstellen, bearbeiten und löschen
- Timer mit Fortschrittsanzeige, Pausieren/Fortsetzen, Vor-/Zurückspringen und Reset
- Persistente Speicherung der eigenen Sequenzen via Jetpack DataStore
- Material 3 Oberfläche mit Unterstützung für Light/Dark-Mode

## Tech Stack

- Kotlin 1.9.22
- Android Gradle Plugin 8.4.0
- Jetpack Compose (BOM 2024.02.00)
- Jetpack DataStore (Preferences) + kotlinx.serialization
- Coroutines für State Management & Timer-Steuerung

## Voraussetzungen

- JDK 17 (z. B. Temurin oder Azul Zulu)
- Android SDK 34 (inkl. Build-Tools 34.0.0)
- Android Studio Iguana oder aktueller **oder** Android Command Line Tools
- (Optional) angeschlossenes Gerät oder Emulator für das Debug-APK

## Projektstruktur

```
asana-app/
├── app/
│   ├── build.gradle.kts            # Modul-Konfiguration
│   ├── src/main/java/com/asana/timer
│   │   ├── data/                   # Datenmodelle & Repository
│   │   ├── ui/                     # Compose Navigation & Screens
│   │   └── ui/theme/               # Farb- & Theme-Setup
│   └── src/main/res                # Ressourcen (Manifest, Strings, Icons)
├── build.gradle.kts                # Root Build Skript
├── gradle/                         # Gradle Wrapper Dateien
├── gradlew / gradlew.bat           # Wrapper-Skripte
└── settings.gradle.kts
```

## Lokales Setup

1. Projekt in Android Studio öffnen oder im Terminal in den Projektordner wechseln.
2. Pfad zum Android SDK in `local.properties` eintragen (Android Studio erstellt die Datei automatisch):

    ```
    sdk.dir=C:\Users\<user>\AppData\Local\Android\Sdk
    ```

3. Abhängigkeiten synchronisieren (`Gradle Sync` oder `./gradlew tasks`).

## APK bauen

**Windows PowerShell:**

```powershell
./gradlew.bat assembleDebug
```

**macOS / Linux:**

```bash
./gradlew assembleDebug
```

Das signierte Debug-APK liegt anschließend unter `app/build/outputs/apk/debug/app-debug.apk` und kann per `adb install app-debug.apk` installiert werden.

## Nützliche Gradle Aufgaben

- `./gradlew lint` – statische Analyse
- `./gradlew test` – Unit Tests (aktuell noch nicht implementiert)
- `./gradlew clean` – Bereinigt Build-Artefakte

## Roadmap / Ideen

- Unit-Tests für Repository und Timer-Logik ergänzen
- Animierte Übergänge & Haptik-Feedback hinzufügen
- Export/Import der Sequenzen als JSON-Datei ermöglichen

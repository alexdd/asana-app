# Yoga Asana Timer (Android, Jetpack Compose)

Eine moderne Android-App zur Verwaltung und Ausführung von Yoga-Sequenzen. Die ursprüngliche Kivy/Buildozer-Basis wurde vollständig durch eine native Kotlin-App ersetzt, die sich lokal zuverlässig als APK bauen lässt.

## Features

- Sequenzen mit beliebig vielen Asanas erstellen, bearbeiten und löschen
- Timer mit Fortschrittsanzeige, Pausieren/Fortsetzen, Vor-/Zurückspringen und Reset
- **Bildschirm bleibt aktiv** während der Timer läuft (kein Lock Screen)
- Persistente Speicherung der eigenen Sequenzen via Jetpack DataStore
- Material 3 Oberfläche mit vollständiger Unterstützung für Light/Dark-Mode
- **XML-basierte Konfiguration** für initiale Sequenzen (beim Build anpassbar)
- Splash Screen mit Link zur Homepage

## Tech Stack

- Kotlin 1.9.23
- Android Gradle Plugin 8.4.0
- Jetpack Compose (BOM 2024.02.00) mit Compose Compiler 1.5.11
- Jetpack DataStore (Preferences) + kotlinx.serialization
- Coroutines für State Management & Timer-Steuerung
- Navigation Compose für Screen-Navigation

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
│   │   ├── data/                   # Datenmodelle & Repository (XML-Parser)
│   │   ├── ui/                     # Compose Navigation & Screens
│   │   └── ui/theme/               # Farb- & Theme-Setup
│   └── src/main/res                # Ressourcen
│       ├── xml/
│       │   └── default_sequences.xml  # Initiale Asana-Sequenzen
│       ├── values/                 # Strings, Themes
│       └── drawable/              # Icons
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

## Initiale Sequenzen konfigurieren

Die App lädt beim ersten Start automatisch vordefinierte Sequenzen aus einer XML-Datei. Diese können vor dem Build angepasst werden:

**Datei:** `app/src/main/res/xml/default_sequences.xml`

### XML-Struktur:

```xml
<?xml version="1.0" encoding="utf-8"?>
<sequences>
    <sequence>
        <name>Sequenz Name</name>
        <asanas>
            <asana>
                <title>Asana Name</title>
                <durationSeconds>60</durationSeconds>
            </asana>
            <!-- Weitere Asanas... -->
        </asanas>
    </sequence>
    <!-- Weitere Sequenzen... -->
</sequences>
```

### Wichtige Hinweise:

- Die Sequenzen werden **nur beim ersten Start** geladen (wenn noch keine Sequenzen gespeichert sind)
- Nach dem ersten Start werden die Sequenzen in DataStore gespeichert
- Um die XML-Sequenzen erneut zu laden, müssen die App-Daten gelöscht werden
- Die XML-Datei wird beim Build in die APK eingebunden

### Beispiel:

```xml
<sequence>
    <name>Morgendliche Praxis</name>
    <asanas>
        <asana>
            <title>Mountain Pose</title>
            <durationSeconds>30</durationSeconds>
        </asana>
        <asana>
            <title>Sun Salutation</title>
            <durationSeconds>180</durationSeconds>
        </asana>
    </asanas>
</sequence>
```

## Nützliche Gradle Aufgaben

- `./gradlew lint` – statische Analyse
- `./gradlew test` – Unit Tests (aktuell noch nicht implementiert)
- `./gradlew clean` – Bereinigt Build-Artefakte
- `./gradlew installDebug` – Baut und installiert die App automatisch auf angeschlossenem Gerät/Emulator

## Weitere Features

### Splash Screen
- Wird 5 Sekunden angezeigt
- Enthält einen klickbaren Link zur Homepage: [www.publiziere.de # yoga](https://www.stylesheet-entwicklung.de/publiziere/#/yoga)
- Gradient-Hintergrund mit App-Farben

### Timer-Funktionen
- **Screen-On**: Der Bildschirm bleibt aktiv, solange der Timer läuft
- Automatischer Wechsel zwischen Asanas
- Fortschrittsanzeige mit LinearProgressIndicator
- Anzeige der nächsten Asana

### Dark Theme
- Vollständige Unterstützung für Light/Dark-Mode
- Automatische Anpassung basierend auf System-Einstellungen
- Alle UI-Elemente (Cards, Eingabefelder, Texte) sind in beiden Themes lesbar

## Roadmap / Ideen

- Unit-Tests für Repository und Timer-Logik ergänzen
- Animierte Übergänge & Haptik-Feedback hinzufügen
- Export/Import der Sequenzen als JSON-Datei ermöglichen

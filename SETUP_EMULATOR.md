# Android Emulator Setup Guide

## Option 1: Android Studio (Einfachste Methode)

### 1. Android Studio installieren (falls noch nicht vorhanden)
- Download: https://developer.android.com/studio
- Installieren und öffnen

### 2. AVD (Android Virtual Device) erstellen
1. In Android Studio: **Tools → Device Manager**
2. Klicke auf **Create Device**
3. Wähle ein Gerät (z.B. **Pixel 5**)
4. Wähle ein System Image (z.B. **API 34 - Android 14**)
   - Falls nicht installiert: Klicke auf **Download** neben dem System Image
5. Klicke **Next** → **Finish**

### 3. Emulator starten
- In Device Manager: Klicke auf ▶️ **Play** neben dem erstellten AVD
- Warte bis der Emulator vollständig gestartet ist

### 4. App installieren
```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" install app\build\outputs\apk\debug\app-debug.apk
```

---

## Option 2: Command Line Tools (Für Fortgeschrittene)

### 1. Emulator und System Images installieren
```powershell
# SDK Manager Pfad
$sdkManager = "$env:ANDROID_HOME\cmdline-tools\latest\bin\sdkmanager.bat"

# Emulator installieren
& $sdkManager "emulator"

# System Image installieren (z.B. Android 14 API 34)
& $sdkManager "system-images;android-34;google_apis;x86_64"
```

### 2. AVD erstellen
```powershell
$avdManager = "$env:ANDROID_HOME\cmdline-tools\latest\bin\avdmanager.bat"

# AVD erstellen
& $avdManager create avd -n "Pixel5_API34" -k "system-images;android-34;google_apis;x86_64" -d "pixel_5"
```

### 3. Emulator starten
```powershell
& "$env:ANDROID_HOME\emulator\emulator.exe" -avd Pixel5_API34
```

---

## Schnelltest: Prüfe ob Emulator läuft

```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" devices
```

Sollte zeigen:
```
List of devices attached
emulator-5554    device
```

---

## App direkt im Emulator installieren

```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" install app\build\outputs\apk\debug\app-debug.apk
```

---

## Tipp: Gradle kann auch direkt installieren

```powershell
.\gradlew.bat installDebug
```

Dies baut die App UND installiert sie automatisch auf dem angeschlossenen Gerät/Emulator!


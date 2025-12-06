# Releases Verzeichnis

Dieses Verzeichnis wird verwendet, um lokal gebaute APK-Dateien für GitHub Releases bereitzustellen.

## Workflow (Automatisiert)

1. **APK lokal bauen** - Die Schritte 2 und 3 werden automatisch ausgeführt:
   ```powershell
   # Windows
   ./gradlew.bat assembleRelease
   
   # Linux/macOS
   ./gradlew assembleRelease
   ```

   Der Build-Task führt automatisch aus:
   - ✓ Kopiert die APK nach `releases/app-release-v{version}.apk`
   - ✓ Fügt die APK zu Git hinzu

2. **Commit und Push:**
   ```powershell
   git commit -m "chore: add APK for release"
   git push
   ```

3. **Automatischer Release:** Der GitHub Workflow erstellt automatisch ein Release mit der Version aus `app/build.gradle.kts` und lädt die APK-Datei hoch. Nach erfolgreichem Release werden die APK-Dateien automatisch aus dem Repository entfernt.

## Manueller Workflow (falls benötigt)

Falls Sie den Task manuell ausführen möchten:
```powershell
./gradlew.bat copyApkToReleases
```

## Wichtig

- Die APK wird automatisch nach dem Build kopiert und zu Git hinzugefügt
- Die Version wird automatisch aus `app/build.gradle.kts` (versionName) extrahiert
- Build-Artefakte bleiben lokal (werden durch `.gitignore` ignoriert)
- Nach dem Release werden die APK-Dateien automatisch aus dem Repository entfernt


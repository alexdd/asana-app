# Android Emulator Setup Script
# Dieses Skript installiert den Emulator und erstellt ein AVD

$ErrorActionPreference = "Stop"

Write-Host "=== Android Emulator Setup ===" -ForegroundColor Cyan
Write-Host ""

# Prüfe Android SDK
if (-not $env:ANDROID_HOME) {
    Write-Host "Fehler: ANDROID_HOME ist nicht gesetzt!" -ForegroundColor Red
    exit 1
}

$sdkManager = "$env:ANDROID_HOME\cmdline-tools\latest\bin\sdkmanager.bat"
$avdManager = "$env:ANDROID_HOME\cmdline-tools\latest\bin\avdmanager.bat"

if (-not (Test-Path $sdkManager)) {
    Write-Host "Fehler: SDK Manager nicht gefunden unter: $sdkManager" -ForegroundColor Red
    Write-Host "Bitte installiere Android Studio oder die Command Line Tools." -ForegroundColor Yellow
    exit 1
}

# Schritt 1: Emulator installieren
Write-Host "Schritt 1/3: Installiere Android Emulator..." -ForegroundColor Yellow
& $sdkManager "emulator" --sdk_root=$env:ANDROID_HOME

if ($LASTEXITCODE -ne 0) {
    Write-Host "Fehler beim Installieren des Emulators." -ForegroundColor Red
    exit 1
}

# Schritt 2: System Image installieren (Android 14 API 34)
Write-Host ""
Write-Host "Schritt 2/3: Installiere Android 14 System Image..." -ForegroundColor Yellow
Write-Host "Dies kann einige Minuten dauern..." -ForegroundColor Gray
& $sdkManager "system-images;android-34;google_apis;x86_64" --sdk_root=$env:ANDROID_HOME

if ($LASTEXITCODE -ne 0) {
    Write-Host "Fehler beim Installieren des System Images." -ForegroundColor Red
    exit 1
}

# Schritt 3: AVD erstellen
Write-Host ""
Write-Host "Schritt 3/3: Erstelle Android Virtual Device..." -ForegroundColor Yellow

$avdName = "Pixel5_API34"
& $avdManager create avd -n $avdName -k "system-images;android-34;google_apis;x86_64" -d "pixel_5" --sdk_root=$env:ANDROID_HOME

if ($LASTEXITCODE -ne 0) {
    Write-Host "Fehler beim Erstellen des AVD." -ForegroundColor Red
    Write-Host "Versuche es manuell mit:" -ForegroundColor Yellow
    Write-Host "  & `"$avdManager`" create avd -n $avdName -k `"system-images;android-34;google_apis;x86_64`" -d `"pixel_5`"" -ForegroundColor Gray
    exit 1
}

Write-Host ""
Write-Host "=== Setup abgeschlossen! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Emulator starten mit:" -ForegroundColor Cyan
Write-Host "  & `"$env:ANDROID_HOME\emulator\emulator.exe`" -avd $avdName" -ForegroundColor White
Write-Host ""
Write-Host "Oder App direkt installieren (startet Emulator automatisch):" -ForegroundColor Cyan
Write-Host "  .\gradlew.bat installDebug" -ForegroundColor White


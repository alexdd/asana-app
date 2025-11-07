# Prüft, ob ein Android-Gerät angeschlossen ist
$adbPath = "$env:ANDROID_HOME\platform-tools\adb.exe"

if (-not (Test-Path $adbPath)) {
    Write-Host "ADB nicht gefunden unter: $adbPath" -ForegroundColor Red
    exit 1
}

Write-Host "Suche nach angeschlossenen Geräten..." -ForegroundColor Yellow
& $adbPath devices

$devices = & $adbPath devices | Select-String -Pattern "device$"
if ($devices) {
    Write-Host "`nGerät gefunden! Du kannst jetzt das APK installieren mit:" -ForegroundColor Green
    Write-Host "  adb install app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan
} else {
    Write-Host "`nKein Gerät gefunden. Bitte:" -ForegroundColor Yellow
    Write-Host "  1. USB-Debugging am Gerät aktivieren" -ForegroundColor White
    Write-Host "  2. Gerät per USB anschließen" -ForegroundColor White
    Write-Host "  3. 'USB-Debugging zulassen' am Gerät bestätigen" -ForegroundColor White
}


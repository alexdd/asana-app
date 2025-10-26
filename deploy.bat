@echo off
REM Quick deploy to GitHub
echo.
echo ========================================
echo  Yoga Asana Timer - GitHub Deploy
echo ========================================
echo.

REM Prüfe ob GitHub remote existiert
git remote show origin >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ Git remote bereits konfiguriert
    echo.
    echo Pushe zu GitHub...
    git push origin master
    echo.
    echo ✅ Code ist auf GitHub!
    echo.
    echo 🌐 Gehe jetzt zu:
    echo   https://github.com/[DEIN-USERNAME]/yoga-asana-timer/actions
    echo.
    echo   Und klicke "Run workflow"
) else (
    echo ✗ GitHub remote nicht konfiguriert
    echo.
    echo Bitte manuell konfigurieren:
    echo.
    echo   1. Gehe zu https://github.com/new
    echo   2. Erstelle ein neues Repository
    echo   3. Führe aus:
    echo.
    echo      git remote add origin https://github.com/[USER]/[REPO].git
    echo      git push -u origin master
    echo.
)

echo.
pause


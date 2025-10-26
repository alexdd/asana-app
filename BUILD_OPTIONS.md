# 🚀 Android Build Optionen

Du hast mehrere Möglichkeiten, die Yoga Asana Timer App für Android zu bauen:

## 🎯 Option 1: GitHub Actions (Empfohlen - Einfachste Methode)

### Vorteile:
- ✅ Keine lokale Installation nötig
- ✅ Vollautomatisch
- ✅ APK als Download verfügbar

### So geht's:

1. **Code auf GitHub pushen**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/DEIN-USERNAME/asana-app.git
   git push -u origin main
   ```

2. **Auf GitHub:**
   - Gehe zu: https://github.com/DEIN-USERNAME/asana-app/actions
   - Klicke "Run workflow"
   - Warte ca. 15-20 Minuten
   - Lade die APK aus den "Artifacts" herunter

3. **APK installieren:**
   - Datei auf Android-Gerät kopieren
   - Installieren über Datei-Manager

## 🖥️ Option 2: WSL + Ubuntu (Lokal auf Windows)

### Vorteile:
- ✅ Vollständige Kontrolle
- ✅ Kann mehrmals bauen ohne Limit

### So geht's:

1. **WSL installieren** (einmalig):
   ```powershell
   # PowerShell als Administrator
   wsl --install
   # Windows neu starten
   ```

2. **Ubuntu öffnen** und ausführen:
   ```bash
   cd /mnt/c/work/asana-app
   chmod +x build.sh
   ./build.sh    # Dauert ca. 15 Minuten beim ersten Mal
   ```

3. **APK bauen:**
   ```bash
   ./quick_build.sh    # Oder: buildozer android debug
   ```

4. **APK installieren:**
   ```bash
   adb install bin/*.apk
   ```

## 🐳 Option 3: Docker

Wenn Sie Docker installiert haben:

```bash
docker run --rm -it \
  -v "$PWD":/hostcwd \
  kivy/buildozer bash
  
cd /hostcwd
buildozer android debug
exit

# APK befindet sich in bin/
```

## ⚡ Option 4: Colab (Schnell Test)

Google Colab kann auch für Builds verwendet werden (experimentell).

## 📊 Vergleich

| Methode | Einfachheit | Geschwindigkeit | Einstieg |
|---------|-------------|-----------------|----------|
| GitHub Actions | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 5 Min |
| WSL | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 30 Min |
| Docker | ⭐⭐ | ⭐⭐⭐ | 20 Min |

## 🎯 Empfehlung für dich

**Am einfachsten: GitHub Actions** verwenden!

1. Einfach auf GitHub pushen
2. APK automatisch gebaut
3. Fertig! ✨

Soll ich dir dabei helfen?

```bash
# Um auf GitHub zu pushen:
git init
git add .
git commit -m "Yoga Asana Timer App"
git remote add origin https://github.com/DEIN-USERNAME/asana-app.git
git push -u origin main
```

Dann auf GitHub → Actions → Run workflow

## 📝 Alternative: Ich erstelle ein Cloud-Template

Falls GitHub nicht gewünscht ist, kann ich auch ein:
- GitLab CI
- Azure DevOps
- Oder ein simples Cloud-Setup erstellen

Welche Option bevorzugst du?


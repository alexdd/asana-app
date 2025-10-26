# 🚀 Quick Android Build Guide

## Option 1: Automatisches Setup (Empfohlen)

### Schritt 1: WSL + Ubuntu installieren
```powershell
# PowerShell als Administrator öffnen
wsl --install -d Ubuntu

# Nach der Installation Windows neu starten
# Dann Ubuntu starten und Benutzername/Passwort setzen
```

### Schritt 2: Build-Skript ausführen
```bash
# In Ubuntu WSL:
cd /mnt/c/work/asana-app
chmod +x build.sh
./build.sh

# Das dauert ca. 10-15 Minuten (erste Installation)
```

### Schritt 3: APK bauen
```bash
# Wenn Setup fertig ist:
./quick_build.sh

# Oder manuell:
buildozer android debug
```

### Schritt 4: APK installieren
```bash
# Android-Gerät via USB verbinden
adb devices  # Sollte Ihr Gerät zeigen

# APK installieren
adb install bin/yogaasanatimer-*.apk
```

## Option 2: Manueller Build

```bash
# 1. WSL öffnen und ins Projektverzeichnis
cd /mnt/c/work/asana-app

# 2. System aktualisieren
sudo apt update && sudo apt upgrade -y

# 3. Dependencies installieren
sudo apt install -y python3 python3-pip build-essential git zip unzip openjdk-11-jdk

# 4. Android SDK einrichten
mkdir -p ~/Android/Sdk/cmdline-tools/latest
# Downloaden Sie: https://developer.android.com/studio#command-tools
# Entpacken Sie nach ~/Android/Sdk/cmdline-tools/latest/

# 5. Umgebungsvariablen
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools' >> ~/.bashrc

# 6. Android SDK installieren
yes | sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-31" "build-tools;31.0.0"

# 7. Buildozer installieren
pip3 install --user buildozer
export PATH=$PATH:$HOME/.local/bin

# 8. APK bauen
buildozer android debug

# 9. APK installieren
adb install bin/*.apk
```

## Alternative: Online Build Services

Falls WSL nicht funktioniert, können Sie auch verwenden:

### 1. **GitHub Actions** (kostenlos)
Ich kann ein GitHub Actions Workflow erstellen, der automatisch baut.

### 2. **Google Colab**
- Jupyter Notebook in der Cloud
- Linux-Umgebung
- Könnte für Build verwendet werden

### 3. **Docker** (falls installiert)
```bash
docker run -it -v "$PWD":/home/user/hostcwd kivy/buildozer bash
```

## Tipps

- **Erster Build dauert**: 30-45 Minuten (SDK Download)
- **Nachfolgende Builds**: 2-5 Minuten
- **APK-Größe**: Ca. 10-15 MB

## Probleme?

**Problem**: `wsl command not found`
```powershell
# Als Administrator:
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
```

**Problem**: `java not found`
```bash
sudo apt install openjdk-11-jdk
```

**Problem**: Buildozer findet Android SDK nicht
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
```


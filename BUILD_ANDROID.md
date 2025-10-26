# Android Build Anleitung

## Voraussetzungen für Windows

### 1. WSL und Ubuntu installieren

Öffnen Sie PowerShell als Administrator und führen Sie aus:

```powershell
wsl --install
```

Nach der Installation müssen Sie Windows neu starten.

### 2. Ubuntu starten und Setup durchführen

```bash
# Ubuntu im Startmenü starten, dann:
sudo apt update
sudo apt upgrade -y

# Python und pip installieren
sudo apt install -y python3 python3-pip python3-venv git

# Android SDK installieren
sudo apt install -y openjdk-11-jdk
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
mkdir -p ~/Android/Sdk/cmdline-tools
mv cmdline-tools ~/Android/Sdk/cmdline-tools/latest

# Umgebungsvariablen setzen
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools' >> ~/.bashrc
source ~/.bashrc

# Android SDK installieren
yes | sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-31" "build-tools;31.0.0"
```

### 3. Buildozer im WSL installieren

```bash
# Ins Projektverzeichnis wechseln (Windows Mount)
cd /mnt/c/work/asana-app

# Buildozer installieren
pip3 install --user buildozer

# PATH aktualisieren (optional)
echo 'export PATH=$PATH:$HOME/.local/bin' >> ~/.bashrc
source ~/.bashrc
```

### 4. Android APK bauen

```bash
# APK erstellen
buildozer android debug

# Die APK-Datei befindet sich in:
# bin/*.apk
```

### 5. APK auf Android-Gerät installieren

```bash
# USB-Debugging auf dem Android-Gerät aktivieren
# USB verbinden, dann:
adb devices  # Sollte Ihr Gerät anzeigen
adb install bin/*.apk

# Oder manuell auf dem Gerät:
# APK-Datei auf Gerät kopieren (z.B. via USB/Dropbox)
# Datei-Manager öffnen, APK-Datei tippen
```

## Alternative: Build ohne WSL

### Option 1: Linux VM
- VirtualBox oder VMware installieren
- Ubuntu Linux installieren
- Dann wie oben beschrieben mit Buildozer bauen

### Option 2: Cloud Build
Verwenden Sie einen Cloud-Service wie:
- Python-for-Android Docker Images
- Oder eine Linux-Cloud-Instanz

## Schnellstart (wenn WSL bereits installiert)

```bash
# WSL öffnen
wsl

# Ins Projektverzeichnis
cd /mnt/c/work/asana-app

# Buildozer installieren (falls noch nicht geschehen)
pip3 install --user buildozer

# APK bauen
buildozer android debug

# Installieren (Gerät via USB verbunden)
adb install bin/*.apk
```

## Fehlerbehebung

### "command not found: buildozer"
```bash
export PATH=$PATH:$HOME/.local/bin
```

### "Unable to locate Android SDK"
```bash
export ANDROID_HOME=$HOME/Android/Sdk
```

### Weitere Probleme
Siehe Buildozer-Dokumentation: https://buildozer.readthedocs.io/


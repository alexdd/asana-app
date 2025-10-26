#!/bin/bash
# Android Build Script für Yoga Asana Timer

echo "🧘 Yoga Asana Timer - Android Build"
echo "===================================="

# Prüfe ob wir in WSL sind
if [ -z "$WSL_DISTRO_NAME" ]; then
    echo "⚠️  Dieses Skript sollte in WSL/Ubuntu ausgeführt werden"
fi

# Aktualisiere System
echo "📦 System aktualisieren..."
sudo apt update -qq
sudo apt upgrade -y

# Python installieren
echo "🐍 Python installieren..."
sudo apt install -y python3 python3-pip python3-venv git zip unzip

# Java installieren (für Android SDK)
echo "☕ Java installieren..."
sudo apt install -y openjdk-11-jdk

# Android SDK vorbereiten
echo "📱 Android SDK einrichten..."
mkdir -p ~/Android/Sdk/cmdline-tools/latest

# Android Command Line Tools herunterladen
echo "⬇️  Android SDK Tools herunterladen..."
cd /tmp
if [ ! -f commandlinetools-linux-latest.zip ]; then
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O commandlinetools-linux-latest.zip
fi

unzip -q -o commandlinetools-linux-latest.zip -d cmdline-tools
mv cmdline-tools/* ~/Android/Sdk/cmdline-tools/latest/

# Umgebungsvariablen setzen
echo "🔧 Umgebungsvariablen konfigurieren..."
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

grep -q "ANDROID_HOME" ~/.bashrc || echo "export ANDROID_HOME=\$HOME/Android/Sdk" >> ~/.bashrc
grep -q "Android SDK" ~/.bashrc || echo "export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools" >> ~/.bashrc

# Android SDK installieren
echo "📥 Android SDK Komponenten installieren (das kann einige Minuten dauern)..."
yes | sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-31" "build-tools;31.0.0"

# Buildozer installieren
echo "🔨 Buildozer installieren..."
pip3 install --user buildozer
export PATH=$PATH:$HOME/.local/bin

# Ins Projektverzeichnis wechseln
cd /mnt/c/work/asana-app || cd $(dirname "$0")

echo ""
echo "✅ Setup abgeschlossen!"
echo ""
echo "Jetzt können Sie das Android APK bauen:"
echo "  buildozer android debug"
echo ""
echo "Die APK-Datei wird in bin/ gespeichert"


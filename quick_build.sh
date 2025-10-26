#!/bin/bash
# Schneller Build - falls bereits alles installiert ist

cd /mnt/c/work/asana-app || cd $(dirname "$0")

# Prüfe ob buildozer existiert
if ! command -v buildozer &> /dev/null; then
    echo "❌ Buildozer nicht gefunden. Führe zuerst ./build.sh aus"
    exit 1
fi

echo "🔨 Baue Android APK..."
export PATH=$PATH:$HOME/.local/bin
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

buildozer android debug

if [ -f "bin/*.apk" ]; then
    echo ""
    echo "✅ APK erfolgreich gebaut!"
    ls -lh bin/*.apk
    echo ""
    echo "📲 Installieren mit: adb install bin/*.apk"
else
    echo "❌ Build fehlgeschlagen. Prüfen Sie die Ausgabe oben."
fi


#!/bin/bash
# Yoga Asana Timer - Android Build Script
# This script builds the APK in WSL/Ubuntu

set -e  # Exit on error

echo "🧘 Yoga Asana Timer - Android Build"
echo "===================================="

cd /mnt/c/work/asana-app

# Accept all necessary licenses
echo "📜 Accepting Android licenses..."
mkdir -p ~/.android/licenses
echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > ~/.android/licenses/android-sdk-license
echo "601085b94cd77f0b54ff86406957099ebe79c4d6" > ~/.android/licenses/android-sdk-preview-license
echo "d56f5187479451eabf01fb78af6dfcb131a6211b" >> ~/.android/licenses/android-sdk-license
echo "33b8a2de1fa06cd4ef4975e4210084aae9f89aa0" >> ~/.android/licenses/android-sdk-license
echo "841af874906b3d17be73fe81cf8ae50ff9f13fb7" >> ~/.android/licenses/android-sdk-license

# Set environment variables
export PATH=$PATH:$HOME/.local/bin
export ANDROID_HOME=$HOME/Android/Sdk

# Build with automatic "y" confirmation
echo ""
echo "🔨 Starting build (this may take 30-60 minutes for first build)..."
echo ""
echo "y" | buildozer -v android debug

# Check if APK was created
if ls bin/*.apk 1> /dev/null 2>&1; then
    echo ""
    echo "✅ Build successful!"
    ls -lh bin/*.apk
    echo ""
    echo "📲 Install with: adb install bin/*.apk"
else
    echo ""
    echo "❌ Build failed. Check logs above."
    exit 1
fi

# Quick Start Guide

## Installation und Entwicklung

### 1. Python und Kivy installieren

```bash
# Python 3.7+ muss installiert sein
python --version

# Kivy installieren
pip install kivy

# Oder aus requirements.txt
pip install -r requirements.txt
```

### 2. App starten

```bash
python main.py
```

Die App läuft jetzt auf Ihrem Desktop! Sie können sie testen und entwickeln.

## Für Android Build

### Voraussetzungen

1. **Android SDK** installieren
2. **Buildozer** installieren:
   ```bash
   pip install buildozer
   ```

3. **Java JDK** installieren

### Build erstellen

```bash
# Debug APK
buildozer android debug

# Release APK
buildozer android release
```

Die APK Datei befindet sich dann im `bin/` Ordner.

### Auf Android-Gerät installieren

```bash
# USB Debugging aktivieren auf dem Gerät
# Dann:
adb install bin/yogaasanatimer-1.0-arm64-v8a-debug.apk
```

## Funktionen

### Hauptliste
- Zeigt alle gespeicherten Asana-Listen
- Neue Liste erstellen mit [+] Button
- Listen bearbeiten (✎)
- Listen löschen (✕)
- Timer starten durch Klick auf die Liste

### Asana-Liste konfigurieren
- Listenname eingeben
- Asanas hinzufügen mit Namen und Dauer (Sekunden)
- Asanas löschen mit ✕ Button
- Liste speichern

### Timer-Modus
- Großer Countdown-Timer
- Asana Name wird angezeigt
- Fortschritts-Indikatoren für alle Asanas
- ⏸ Pause/Weiter Button
- ❌ Abbrechen Button
- Automatisches Weitergehen zur nächsten Asana

## Beispiel-Listen

Die App kommt mit zwei vorinstallierten Listen:

1. **Morgendliche Sonnengrüße** - Klassische Sequenz
2. **Hüftöffnende Sequenz** - Für mehr Flexibilität

## Technologie

- **Python 3.7+**
- **Kivy 2.1+**
- **Buildozer** für Android Build

## Tipps

- Die Daten werden in `~/.yoga_asana_timer/` gespeichert
- Alle Listen werden in JSON Format gespeichert
- Die App ist auf Querformat (Landscape) optimiert beim Timer
- Unterstützt Portrait und Landscape Mode

## Entwicklung

### Projekt-Struktur

```
.
├── main.py                 # App-Einstiegspunkt
├── data_manager.py         # Datenverwaltung
├── theme.py                # Farben und Design
├── screens/
│   ├── splash_screen.py    # Splash Screen
│   ├── main_list_screen.py # Hauptliste
│   ├── asana_list_config_screen.py  # Konfiguration
│   └── timer_screen.py     # Timer-Modus
└── requirements.txt        # Dependencies
```

### Code-Style

- Python PEP 8 konform
- Chakra-Farben aus `theme.py`
- Modulares Design mit separaten Screens


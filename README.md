# Yoga Asana Timer

Eine elegante Android-App zum Timing von Yoga Asanas mit schlankem Design in Yoga Chakra Farben.

## Features

- ✨ **Splash Screen** beim App-Start
- 📋 **Asana Listen Verwaltung** - Erstelle und verwalte deine eigenen Asana-Sequenzen
- ⏱️ **Countdown Timer** - Großer Timer für jede Asana
- ⏸️ **Pause/Weiter** Funktion
- 🎨 **Chakra-Farben Design** - Elegant und minimalistisch
- 💾 **Persistente Speicherung** - Deine Listen werden gespeichert

## Installation

### Für die Entwicklung (Desktop)

```bash
# Python installieren (3.7+)
# Dann Kivy installieren:
pip install -r requirements.txt

# App starten:
python main.py
```

### Für Android

```bash
# Android SDK und Build Tools installieren
# Buildozer installieren:
pip install buildozer

# Android build erstellen:
buildozer android debug

# Oder für Release:
buildozer android release
```

## Verwendung

### Neue Asana Liste erstellen

1. Beim Start erscheint die Hauptliste
2. Drücke auf "[+] Neue Asana Liste erstellen"
3. Gib einen Namen für deine Liste ein
4. Füge Asanas hinzu mit Namen und Dauer in Sekunden
5. Speichere die Liste

### Timer starten

1. Wähle eine Asana Liste aus der Hauptliste
2. Die App wechselt automatisch zum Timer-Modus
3. Die Zeit zählt automatisch herunter
4. Tippe auf Pause, um anzuhalten
5. Tippe nochmal, um fortzusetzen
6. Nutze "Abbrechen" um zur Liste zurückzukehren

## Vordefinierte Listen

Die App kommt mit zwei Beispiel-Listen:

1. **Morgendliche Sonnengrüße** - Eine klassische Sonnengrüße Sequenz
2. **Hüftöffnende Sequenz** - Asanas für die Hüftöffnung

## Technologie

- **Python** mit **Kivy** Framework
- Minimaler Overhead, einfache Wartung
- Cross-platform (Desktop & Android)


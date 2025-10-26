# 🚀 Push zu GitHub - In 2 Minuten

## Schritt 1: GitHub Repository erstellen

**Option A - Via Web (Am einfachsten):**

1. Gehe zu: https://github.com/new
2. Repository Name: `yoga-asana-timer` (oder was du willst)
3. Klicke **"Create repository"**
4. **WICHTIG:** Kopiere die URL die erscheint (z.B. `https://github.com/DEIN-USERNAME/yoga-asana-timer.git`)

**Option B - Via GitHub CLI (falls installiert):**
```bash
gh repo create yoga-asana-timer --public --source=. --remote=origin --push
```

## Schritt 2: Code pushen

**Im PowerShell/Terminal (hier im Projektordner):**

```bash
git remote add origin https://github.com/DEIN-USERNAME/yoga-asana-timer.git
git branch -M main
git push -u origin main
```

**Falls du nach GitHub-Credentials gefragt wirst:**

### Für Personal Access Token:
1. GitHub → Settings → Developer settings → Personal access tokens
2. Token erstellen (z.B. "yoga-timer-build")
3. Rechte: `repo`, `workflow` aktivieren
4. Token kopieren und als Passwort verwenden

### Oder GitHub CLI verwenden:
```bash
gh auth login
gh repo create yoga-asana-timer --public --source=. --remote=origin --push
```

## Schritt 3: Workflow starten

1. Gehe zu: `https://github.com/DEIN-USERNAME/yoga-asana-timer/actions`
2. Du siehst "Build Android APK" Workflow
3. Klicke "Run workflow" → "Run workflow"
4. Warte 15-20 Minuten
5. Lade die APK aus "Artifacts" herunter

## 🎉 Fertig!

Die APK-Datei wird zum Download verfügbar sein!

## Alternative: Lokaler Build (ohne GitHub)

Falls du es lieber lokal bauen willst:

```bash
# WSL öffnen
wsl

# Im Projekt
cd /mnt/c/work/asana-app
chmod +x build.sh
./build.sh
```

---

**Tipp:** GitHub Actions ist am einfachsten - du musst nur den Code pushen!


<h1 align="center">Refine.AI</h1>

<p align="center">
  <strong>Your AI Writing Assistant, Everywhere</strong><br>
  <em>Powered by Gemini AI + Optional On-Device Processing</em>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=flat-square" />
  <img src="https://img.shields.io/badge/AI-Gemini%20Flash-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Local%20AI-Gemma%202B-orange?style=flat-square" />
</p>

---

## What is Refine.AI?

Refine.AI is a **system-wide writing assistant** that works in any Android app. Select text, tap the floating bubble, and instantly transform your writing with AI-powered suggestions.

### Key Features

| Feature | Description |
|:---|:---|
| **System-Wide** | Works in WhatsApp, Gmail, Slack, Notes, and more |
| **One-Tap Refine** | Select text → Tap bubble → Done |
| **Local AI Mode** | Offline processing with Gemma 2B (~1.5GB) |
| **Privacy First** | Your text, your device, your control |

---

## Intelligent Tones

Choose the perfect voice for every message:

- **Refine** — Fix grammar and enhance clarity
- **Professional** — Executive-ready communication  
- **Casual** — Friendly and relaxed
- **Warm** — Kind and approachable
- **Love** — Affectionate language
- **Emojify** — Add relevant emojis

---

## Tech Stack

```
Frontend UI      →  React Native (Expo)
Core Engine      →  Kotlin AccessibilityService
Cloud AI         →  Google Gemini Flash
Local AI         →  MediaPipe + Gemma 2B (Int4)
```

---

## Quick Start

```bash
# Clone
git clone https://github.com/Akshayykadam/Refine-AI-Your-writing--elevated-by-intelligence.git
cd ai-writing-assistant

# Install
npm install

# Configure (create android/local.properties)
echo "GEMINI_API_KEY=your_key_here" >> android/local.properties

# Build & Run
npx expo prebuild
npx expo run:android
```

---

## Local AI Setup

Want offline processing? Download the AI model directly in the app:

1. Open **Refine.AI**
2. Tap **Download Model** (~1.5 GB)
3. Enable **On-Device** toggle in the overlay

> The model runs entirely on your device — no internet required.

---

## Privacy

- Only processes text you explicitly select
- Password fields are automatically ignored
- Local AI mode keeps all data on-device

---

<p align="center">
  <strong>Refine your world, one word at a time.</strong>
</p>

<p align="center">
  Made by <a href="https://github.com/Akshayykadam">Akshay Kadam</a>
</p>

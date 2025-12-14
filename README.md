# Refine.AI

**Refine.AI** is a premium, system-wide writing assistant for Android that seamlessly integrates into your daily workflow. Designed with a philosophy of "invisible until needed," it brings the power of **Gemini AI** to every text field on your device.

Refine.AI doesn't just check grammar; it transforms your communication, helping you sound more professional, warm, or even romantic with a single tap.

<p align="center">
  <img src="./assets/icon.png" width="120" alt="Refine.AI Logo" />
</p>

## ✨ Magic in Your Workflow

-   **🪄 System-Wide Integration**: Works everywhere you type—WhatsApp, Gmail, Slack, Notes, and more.
-   **👆 Select to Refine**: Just select text, and the magical sparkle bubble appears. No copying, pasting, or app switching.
-   **🖐️ Drag to Dismiss**: Finished? Simply drag the bubble to the bottom "X" to dismiss it instantly.
-   **🎨 Premium Aesthetic**: A stunning, dark-mode first UI inspired by modern design controls.

## 🎭 Intelligent Tones

Refine.AI offers a curated set of tones to match every context:

-   **Refine**: Pure improvement. Fixes grammar and enhances clarity without changing your voice.
-   **Professional**: Polish your drafts into executive-ready communication.
-   **Casual**: Relax the vibe for chats with friends.
-   **Warm**: Make your message sound kind, human, and approachable.
-   **Love**: Add a touch of romance and affection for special moments.
-   **Emojify**: sprinkle relevant emojis to add personality. ⚡️

## 🛠 Tech Stack

-   **Frontend UI**: React Native (Expo) for Settings & Onboarding.
-   **Core Engine**: Native Kotlin (Android AccessibilityService) for system overlay and text manipulation.
-   **AI Engine**: Google Gemini Flash (Latest) for lightning-fast, context-aware responses.

## 🚀 Setup & Installation

### Prerequisites
-   Node.js & npm/yarn
-   Android Studio & SDK
-   Gemini API Key ([Get one here](https://aistudio.google.com/))

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/refine-ai.git
cd refine-ai
```

### 2. Configure API Key
Create a `local.properties` file in the `android/` directory to securely store your API key:
```properties
sdk.dir=/path/to/your/android/sdk
GEMINI_API_KEY=your_actual_api_key_here
```

### 3. Build and Run
```bash
# Install dependencies
npm install

# Build the native project
npx expo prebuild

# Run on Android Device
npx expo run:android
```

## 📖 How to Use

1.  **Enable Permission**: Open Refine.AI and grant the requested Accessibility permission.
2.  **Select Text**: In any app, press and hold to select text.
3.  **Tap the Sparkle**: The glowing Refine bubble will appear next to your cursor.
4.  **Choose Your Vibe**: Select a chip (e.g., *Refine*, *Warm*, *Professional*).
5.  **Refine**: Watch your text transform instantly.
6.  **Replace**: Tap the checkmark to swap your original text with the magic version.

## 🔒 Privacy First

Refine.AI is built with privacy at its core.
-   **On-Device Logic**: The app only "sees" the text you explicitly select.
-   **No Background Snooping**: The accessibility service only activates on specific text selection events.
-   **Secure Transmission**: Text is sent encrypted directly to the Gemini API only when you tap "Rewrite".
-   **Password Safe**: Automatically ignores password fields and sensitive input types.

---

*Refine your world, one word at a time.*

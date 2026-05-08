# Squishy Spaces

<p align="center">
  <img src="mobile/app/src/main/res/drawable/squishy_happy.png" width="140" alt="Squishy the mascot" />
</p>

An Android app that judges your room. You take a photo, pick how "honest" you want the feedback to be, and Squishy (a cute opinionated squid) tells you what's working, what isn't, and what to do about it.

The backend sends the image to OpenAI with a structured prompt and returns scored feedback across six interior design categories: layout, lighting, color harmony, coziness, clutter, and style coherence. The Android app presents the results with some personality.

---

## How to run it

### Backend

Requires Node.js 18+.

```bash
cd backend
npm install
```

Create a `.env` file:

```
OPENAI_API_KEY=your_key_here
PORT=3000
```

Then start the dev server:

```bash
npm run dev
```

The server runs on `http://localhost:3000`. You can verify it's connected with `GET /health`.

### Android app

Open the `mobile/` directory in Android Studio and run the app on an emulator. The app talks to `10.0.2.2:3000`, which Android maps to your host machine's localhost — so the backend just needs to be running when you hit Analyze.

---

## A few design choices worth noting

### The three squid modes

The biggest design decision was making the AI feedback feel different depending on the mode you choose: not just in tone, but in how scores are anchored. In `gentle` mode, an average room comfortably lands around a 7. In `brutal` mode, that same room would land around a 4–5.

This isn't just a system prompt swap. Each mode gets its own scoring rubric with explicit anchor descriptions (what a 3 means, what a 7 means), a separate tone instruction, and different suggestion framing. Gentle suggestions read as "you might enjoy..."; brutal ones read as "the curtains are killing the light. Replace them."

The goal was for each mode to feel genuinely different rather than just louder or quieter.

### State machine navigation

The app has no navigation graph. All screen routing flows from a single sealed class:

```kotlin
sealed class AppState {
    object Home : AppState()
    data class Preview(val uri: Uri) : AppState()
    object Loading : AppState()
    data class Result(val analysis: RoomAnalysis, val imageUri: Uri) : AppState()
    data class Error(val message: String) : AppState()
}
```

`MainActivity` switches on this state and renders the matching screen. The ViewModel owns all transitions. This keeps the flow easy to follow and means there's no back-stack logic to maintain — the app is intentionally linear.

### The manga overlay

When a room scores very high (8–10) or very low (1–4), a brief manga-style impact overlay fires on the result screen: radial speed lines, a big bold "AMAZING!" or "YIKES!!", and a glow in gold or red. It appears for about 1.6 seconds and disappears on its own.

It's a small detail, but it makes the extreme results feel like an event rather than just a number changing.

---

## Stack

| Layer | Tech |
|---|---|
| Backend | Fastify, TypeScript, OpenAI Responses API |
| Schema validation | Zod |
| Android | Kotlin, Jetpack Compose, ViewModel |
| Networking | OkHttp |
| Local storage | SharedPreferences (last 10 analyses) |
| Image loading | Coil |

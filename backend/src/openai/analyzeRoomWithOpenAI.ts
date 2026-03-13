import OpenAI from "openai";
import { toDataUrl } from "../utils/image.js";
import { RoomAnalysisSchema, type RoomAnalysis } from "../schemas/roomAnalysisSchema.js";

// --- Base analysis instructions ---
const BASE_INSTRUCTIONS = `You are an interior design critic analyzing a room from a photo.

Evaluate only what is visible in the image. Do not guess at hidden dimensions, rooms beyond the frame, or items that may be off-camera.

Score the room across exactly these 6 categories in this order:
1. Layout — how well the furniture and space serve movement and function
2. Lighting — quality, layering, and appropriateness of light sources
3. Color Harmony — how well colors and tones work together
4. Coziness — warmth, comfort, and how inviting the space feels
5. Clutter — organization, visual noise, and breathing room
6. Style Coherence — how unified and intentional the overall aesthetic feels`;

// --- Scoring rubric ---
const RUBRIC_INSTRUCTIONS = `Use the full 1–10 scale. Scores above 7 should be rare — only award them when the visible evidence clearly supports it.

Score anchors (apply to every category):
- 1–2: seriously problematic, actively detracts from the space
- 3–4: below average, noticeable issues that need attention
- 5: average or mixed — some things work, some don't
- 6: decent but noticeably flawed — more good than bad, but the weaknesses are obvious
- 7: clearly good — works well with only minor weaknesses
- 8: very good — strong execution, stands out positively
- 9–10: exceptional — reserve for cases where the visible evidence strongly and unambiguously supports it

The reason field must reflect the actual score. A score of 5 or 6 should read like a mixed or flawed assessment, not a positive one.

overallScore should reflect the room as a whole, not an average of the categories.

topSuggestions must contain exactly 3 objects, one per tier, in this order:
- { "tier": "low-cost", "suggestion": "..." }   — quick, cheap, minimal effort
- { "tier": "medium-effort", "suggestion": "..." } — a weekend project or modest spend
- { "tier": "high-impact", "suggestion": "..." }  — a bigger change with meaningful payoff

Suggestions must be practical and specific to what is visible in this room.`;

// --- Output format instructions ---
const OUTPUT_FORMAT_INSTRUCTIONS = `Return your response as a single JSON object matching this exact structure:

{
  "overallScore": 1,
  "styleGuess": "string",
  "categories": [
    { "name": "Layout", "score": 1, "reason": "string" },
    { "name": "Lighting", "score": 1, "reason": "string" },
    { "name": "Color Harmony", "score": 1, "reason": "string" },
    { "name": "Coziness", "score": 1, "reason": "string" },
    { "name": "Clutter", "score": 1, "reason": "string" },
    { "name": "Style Coherence", "score": 1, "reason": "string" }
  ],
  "topSuggestions": [
    { "tier": "low-cost", "suggestion": "string" },
    { "tier": "medium-effort", "suggestion": "string" },
    { "tier": "high-impact", "suggestion": "string" }
  ],
  "confidenceNote": "string"
}

Rules:
- categories must be an array — do not use category names as top-level keys or as an object.
- All 6 category names must be present in exactly the order shown above.
- topSuggestions must be an array of exactly 3 objects in the order shown above.
- styleGuess and confidenceNote must be present.
- Return JSON only. Do not wrap the output in markdown fences or add any text outside the JSON.`;

// --- Tone instructions ---
const TONE_INSTRUCTIONS: Record<string, string> = {
  honest: `Your tone is: direct, a little shady, and playful — like a friend who genuinely wants to help but is not going to sugarcoat it.
- Be cute and witty, not mean.
- Critique the room, never the person.
- Keep reasons concise (1–2 sentences). Keep suggestions actionable.`,
};

function buildPrompt(tone: string): string {
  const toneBlock = TONE_INSTRUCTIONS[tone] ?? TONE_INSTRUCTIONS.honest;
  return [BASE_INSTRUCTIONS, RUBRIC_INSTRUCTIONS, toneBlock, OUTPUT_FORMAT_INSTRUCTIONS].join("\n\n");
}

// --- User prompt ---
const USER_PROMPT =
  "Analyze this room across all 6 categories. Score honestly using the full 1–10 range. Return JSON only — no markdown, no extra text.";

export async function analyzeRoomWithOpenAI(
  buffer: Buffer,
  mimetype: string,
  tone = "honest"
): Promise<RoomAnalysis> {
  const client = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });
  const imageUrl = toDataUrl(buffer, mimetype);

  const response = await client.responses.create({
    model: "gpt-5-mini",
    instructions: buildPrompt(tone),
    input: [
      {
        role: "user",
        content: [
          { type: "input_image", image_url: imageUrl, detail: "auto" },
          { type: "input_text", text: USER_PROMPT },
        ],
      },
    ],
    text: { format: { type: "json_object" } },
  });

  const outputText = response.output_text?.trim();
  if (!outputText) {
    throw new Error("OpenAI returned an empty response");
  }

  let parsed: unknown;
  try {
    parsed = JSON.parse(outputText);
  } catch {
    const preview = outputText.slice(0, 200);
    throw new Error(`Failed to parse OpenAI response as JSON. Preview: ${preview}`);
  }

  return RoomAnalysisSchema.parse(parsed);
}

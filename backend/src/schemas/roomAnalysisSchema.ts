import { z } from "zod";

const scoreField = z.number().int().min(1).max(10);

const CATEGORY_NAMES = [
  "Layout",
  "Lighting",
  "Color Harmony",
  "Coziness",
  "Clutter",
  "Style Coherence",
] as const;

export const RoomAnalysisSchema = z.object({
  overallScore: scoreField,
  styleGuess: z.string(),
  categories: z
    .array(
      z.object({
        name: z.enum(CATEGORY_NAMES),
        score: scoreField,
        reason: z.string(),
      })
    )
    .length(6),
  topSuggestions: z.tuple([
    z.object({ tier: z.literal("low-cost"), suggestion: z.string() }),
    z.object({ tier: z.literal("medium-effort"), suggestion: z.string() }),
    z.object({ tier: z.literal("high-impact"), suggestion: z.string() }),
  ]),
  confidenceNote: z.string(),
});

export type RoomAnalysis = z.infer<typeof RoomAnalysisSchema>;

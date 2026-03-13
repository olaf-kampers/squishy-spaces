import type { FastifyInstance } from "fastify";
import { analyzeRoomWithOpenAI } from "../openai/analyzeRoomWithOpenAI.js";

const SUPPORTED_MIME_TYPES = ["image/jpeg", "image/png", "image/webp"];
const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

export async function registerAnalyzeRoomRoute(app: FastifyInstance) {
  app.post("/analyze-room", async (request, reply) => {
    const file = await request.file();

    if (!file) {
      return reply.status(400).send({ ok: false, error: "No file uploaded" });
    }

    if (!SUPPORTED_MIME_TYPES.includes(file.mimetype)) {
      return reply.status(415).send({
        ok: false,
        error: `Unsupported file type: ${file.mimetype}. Supported types: ${SUPPORTED_MIME_TYPES.join(", ")}`,
      });
    }

    const buffer = await file.toBuffer();

    if (buffer.length > MAX_FILE_SIZE) {
      return reply.status(413).send({ ok: false, error: "File exceeds 10MB limit" });
    }

    try {
      const result = await analyzeRoomWithOpenAI(buffer, file.mimetype);
      return result;
    } catch (err) {
      app.log.error(err);
      return reply.status(500).send({ ok: false, error: "Analysis failed" });
    }
  });
}

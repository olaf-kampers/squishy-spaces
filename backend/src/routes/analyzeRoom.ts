import type { FastifyInstance } from "fastify";
import { analyzeRoomWithOpenAI, parseSquidMode } from "../openai/analyzeRoomWithOpenAI.js";

const SUPPORTED_MIME_TYPES = ["image/jpeg", "image/png", "image/webp"];
const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

export async function registerAnalyzeRoomRoute(app: FastifyInstance) {
  app.post("/analyze-room", async (request, reply) => {
    let fileBuffer: Buffer | undefined;
    let fileMimetype: string | undefined;
    let squidModeRaw: string | undefined;

    for await (const part of request.parts()) {
      if (part.type === "file") {
        fileMimetype = part.mimetype;
        fileBuffer = await part.toBuffer();
      } else if (part.fieldname === "squidMode") {
        squidModeRaw = String(part.value);
      }
    }

    if (!fileBuffer || !fileMimetype) {
      return reply.status(400).send({ ok: false, error: "No file uploaded" });
    }

    if (!SUPPORTED_MIME_TYPES.includes(fileMimetype)) {
      return reply.status(415).send({
        ok: false,
        error: `Unsupported file type: ${fileMimetype}. Supported types: ${SUPPORTED_MIME_TYPES.join(", ")}`,
      });
    }

    if (fileBuffer.length > MAX_FILE_SIZE) {
      return reply.status(413).send({ ok: false, error: "File exceeds 10MB limit" });
    }

    const squidMode = parseSquidMode(squidModeRaw);

    try {
      const result = await analyzeRoomWithOpenAI(fileBuffer, fileMimetype, squidMode);
      return result;
    } catch (err) {
      app.log.error(err);
      return reply.status(500).send({ ok: false, error: "Analysis failed" });
    }
  });
}

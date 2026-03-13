import type { FastifyInstance } from "fastify";

const SUPPORTED_MIME_TYPES = ["image/jpeg", "image/png", "image/webp"];

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

    return {
      ok: true,
      filename: file.filename,
      mimetype: file.mimetype,
      size: buffer.length,
    };
  });
}

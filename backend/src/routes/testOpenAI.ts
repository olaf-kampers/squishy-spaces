import OpenAI from "openai";
import type { FastifyInstance } from "fastify";

const client = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY,
});

export async function registerTestOpenAIRoute(app: FastifyInstance) {
  app.get("/test-openai", async (_request, reply) => {
    try {
      const response = await client.responses.create({
        model: "gpt-5-mini",
        input: "Reply with only the text: Squishy Spaces backend is connected.",
      });

      return {
        ok: true,
        output: response.output_text,
      };
    } catch (error) {
      app.log.error(error);
      return reply.status(500).send({
        ok: false,
        error: "OpenAI request failed",
      });
    }
  });
}
import "dotenv/config";
import Fastify from "fastify";
import multipart from "@fastify/multipart";
import { registerTestOpenAIRoute } from "./routes/testOpenAI";
import { registerAnalyzeRoomRoute } from "./routes/analyzeRoom";

async function start() {
  const app = Fastify({
    logger: true,
  });

  await app.register(multipart);

  app.get("/health", async () => {
    return {
      ok: true,
      hasOpenAiKey: Boolean(process.env.OPENAI_API_KEY),
    };
  });

  await registerAnalyzeRoomRoute(app);
  await registerTestOpenAIRoute(app);

  const port = Number(process.env.PORT || 3000);

  try {
    await app.listen({ port, host: "0.0.0.0" });
    console.log(`Server running on http://localhost:${port}`);
  } catch (err) {
    app.log.error(err);
    process.exit(1);
  }
}

start();
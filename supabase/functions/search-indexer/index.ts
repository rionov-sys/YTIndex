// Setup type definitions for Supabase Edge Functions (Deno runtime)
// Ref: https://supabase.com/docs/guides/functions
declare const Deno: {
  serve: (handler: (req: Request) => Promise<Response> | Response) => void;
};

interface RequestPayload {
  query?: string;
  limit?: number;
  resultsCount?: number;
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") {
    return new Response("ok", {
      headers: {
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "POST, OPTIONS",
        "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
      },
    });
  }

  try {
    const payload: RequestPayload = await req.json();
    const { query, limit = 10, resultsCount = 0 } = payload;

    if (!query) {
      return new Response(JSON.stringify({ error: "Missing search query" }), {
        status: 400,
        headers: { "Content-Type": "application/json" },
      });
    }

    // Response stub for indexer edge function
    return new Response(
      JSON.stringify({
        status: "success",
        indexedQuery: query.trim(),
        limit,
        resultsCount,
        timestamp: new Date().toISOString(),
      }),
      {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
      }
    );
  } catch (error) {
    return new Response(JSON.stringify({ error: (error as Error).message }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }
});

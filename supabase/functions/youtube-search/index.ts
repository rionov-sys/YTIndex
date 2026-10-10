// ==============================================================================
// Supabase Edge Function: youtube-search (Deno + TypeScript)
// YT Index - Personal YouTube Video Search with Caching & Rate Limit Handling
// ==============================================================================

import { createClient } from "jsr:@supabase/supabase-js@2";

// Deklarasi tipe Deno untuk kompatibilitas editor & IDE tanpa Deno LSP global
declare const Deno: {
  env: {
    get: (key: string) => string | undefined;
  };
  serve: (handler: (req: Request) => Promise<Response> | Response) => void;
};

// Header CORS standar untuk komunikasi klien Android / Web
const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
};

interface RequestBody {
  query?: string;
  limit?: number;
}

interface VideoResultItem {
  videoId: string;
  title: string;
  channel: string;
  publishedAt: string;
  thumbnailUrl: string;
  url: string;
}

/**
 * Decode entitas HTML pada judul video dan channel (mis. &amp; -> &, &#39; -> ')
 */
function decodeHtmlEntities(str: string): string {
  if (!str) return "";
  return str
    .replace(/&amp;/g, "&")
    .replace(/&lt;/g, "<")
    .replace(/&gt;/g, ">")
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
    .replace(/&#039;/g, "'")
    .replace(/&apos;/g, "'")
    .replace(/&#x2F;/g, "/")
    .replace(/&#(\d+);/g, (_, dec) => {
      try {
        return String.fromCharCode(parseInt(dec, 10));
      } catch {
        return _;
      }
    })
    .replace(/&#x([0-9a-fA-F]+);/g, (_, hex) => {
      try {
        return String.fromCharCode(parseInt(hex, 16));
      } catch {
        return _;
      }
    });
}

Deno.serve(async (req: Request) => {
  // 1. Tangani CORS preflight
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }

  if (req.method !== "POST") {
    return new Response(
      JSON.stringify({ error: "Metode HTTP tidak didukung. Gunakan POST." }),
      {
        status: 405,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  }

  // 2. Verifikasi Token JWT Pengguna
  const authHeader = req.headers.get("Authorization");
  if (!authHeader) {
    return new Response(
      JSON.stringify({
        error: "Autentikasi diperlukan. Header Authorization tidak ditemukan.",
      }),
      {
        status: 401,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  }

  const supabaseUrl = Deno.env.get("SUPABASE_URL");
  const supabaseAnonKey = Deno.env.get("SUPABASE_ANON_KEY");
  const supabaseServiceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
  const youtubeApiKey = Deno.env.get("YOUTUBE_API_KEY");

  if (!supabaseUrl || !supabaseAnonKey || !supabaseServiceRoleKey) {
    return new Response(
      JSON.stringify({
        error: "Konfigurasi server database Supabase belum lengkap.",
      }),
      {
        status: 500,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  }

  // Client dengan konteks user (menggunakan token user untuk validasi & RLS)
  const userClient = createClient(supabaseUrl, supabaseAnonKey, {
    global: { headers: { Authorization: authHeader } },
  });

  const {
    data: { user },
    error: authError,
  } = await userClient.auth.getUser();

  if (authError || !user) {
    return new Response(
      JSON.stringify({
        error: "Token JWT tidak valid atau telah kedaluwarsa. Silakan login kembali.",
      }),
      {
        status: 401,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  }

  // Client dengan hak akses service_role (untuk search_cache yang tidak memiliki RLS policy terbuka)
  const adminClient = createClient(supabaseUrl, supabaseServiceRoleKey);

  try {
    // 3. Validasi Input JSON
    const body: RequestBody = await req.json().catch(() => ({}));
    const rawQuery = typeof body.query === "string" ? body.query.trim() : "";
    const rawLimit = typeof body.limit === "number" ? body.limit : 10;

    if (!rawQuery || rawQuery.length < 1 || rawQuery.length > 100) {
      return new Response(
        JSON.stringify({
          error: "Kata kunci pencarian tidak valid. Panjang wajib antara 1 hingga 100 karakter.",
        }),
        {
          status: 400,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }

    // Batasi limit integer antara 1 dan 50 (default 10)
    const limit = Math.min(Math.max(Math.floor(rawLimit), 1), 50);

    // 4. Catat riwayat pencarian pengguna ke tabel search_history
    await userClient
      .from("search_history")
      .insert({
        user_id: user.id,
        query: rawQuery,
        result_limit: limit,
      })
      .select()
      .maybeSingle()
      .catch(() => {
        // Logging histori bersifat non-blocking terhadap alur pencarian utama
      });

    // 5. Cek Cache (search_cache)
    // Format cache_key: query lowercase yang sudah di-trim + tanggal UTC hari ini (YYYY-MM-DD)
    const todayUtc = new Date().toISOString().slice(0, 10);
    const cacheKey = `${rawQuery.toLowerCase()}_${todayUtc}`;

    const { data: cacheRow, error: cacheSelectError } = await adminClient
      .from("search_cache")
      .select("payload, created_at")
      .eq("cache_key", cacheKey)
      .maybeSingle();

    if (!cacheSelectError && cacheRow && Array.isArray(cacheRow.payload)) {
      const createdAt = new Date(cacheRow.created_at).getTime();
      const isFresh = Date.now() - createdAt < 24 * 60 * 60 * 1000;

      if (isFresh) {
        const cachedItems = cacheRow.payload as VideoResultItem[];
        const sliced = cachedItems.slice(0, limit);
        return new Response(
          JSON.stringify({
            items: sliced,
            cached: true,
          }),
          {
            status: 200,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }
    }

    // 6. Cache Miss -> Panggil YouTube Data API v3 (search.list)
    if (!youtubeApiKey) {
      return new Response(
        JSON.stringify({
          error: "YOUTUBE_API_KEY belum dikonfigurasi pada environment Supabase.",
        }),
        {
          status: 500,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }

    // publishedAfter = 365 hari yang lalu (RFC 3339)
    const publishedAfter = new Date(Date.now() - 365 * 24 * 60 * 60 * 1000).toISOString();

    const ytUrl = new URL("https://www.googleapis.com/youtube/v3/search");
    ytUrl.searchParams.set("part", "snippet");
    ytUrl.searchParams.set("type", "video");
    ytUrl.searchParams.set("order", "relevance");
    ytUrl.searchParams.set("maxResults", "50"); // Ambil 50 untuk disimpan ke cache harian
    ytUrl.searchParams.set("publishedAfter", publishedAfter);
    ytUrl.searchParams.set("relevanceLanguage", "id");
    ytUrl.searchParams.set("regionCode", "ID");
    ytUrl.searchParams.set("q", rawQuery);
    ytUrl.searchParams.set("key", youtubeApiKey);

    const ytResponse = await fetch(ytUrl.toString(), {
      method: "GET",
      headers: { Accept: "application/json" },
    });

    if (!ytResponse.ok) {
      const status = ytResponse.status;
      const errorText = await ytResponse.text();

      // Tangani batas kuota YouTube (403 quotaExceeded) -> Map ke HTTP 429
      if (
        status === 403 &&
        (errorText.includes("quotaExceeded") ||
          errorText.includes("dailyLimitExceeded") ||
          errorText.includes("rateLimitExceeded"))
      ) {
        return new Response(
          JSON.stringify({
            error:
              "Batas kuota harian YouTube Data API telah tercapai (quotaExceeded). Silakan coba lagi besok atau gunakan video yang telah tersimpan.",
            code: "QUOTA_EXCEEDED",
          }),
          {
            status: 429,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      if (status === 400) {
        return new Response(
          JSON.stringify({
            error: "Format parameter pencarian ke YouTube API tidak valid.",
            code: "BAD_UPSTREAM_REQUEST",
          }),
          {
            status: 400,
            headers: { ...corsHeaders, "Content-Type": "application/json" },
          }
        );
      }

      // Jangan bocorkan API key atau pesan error mentah ke klien
      return new Response(
        JSON.stringify({
          error: "Gagal mengambil data dari YouTube Data API. Silakan coba beberapa saat lagi.",
          code: "UPSTREAM_SERVICE_ERROR",
        }),
        {
          status: 502,
          headers: { ...corsHeaders, "Content-Type": "application/json" },
        }
      );
    }

    const ytData = await ytResponse.json();
    const rawItems: any[] = Array.isArray(ytData.items) ? ytData.items : [];

    // Map 50 items sesuai spesifikasi yang diminta
    const mappedItems: VideoResultItem[] = rawItems
      .map((item): VideoResultItem | null => {
        const videoId = item?.id?.videoId;
        if (!videoId || typeof videoId !== "string") return null;

        const snippet = item?.snippet || {};
        const thumbs = snippet?.thumbnails || {};
        const thumbUrl =
          thumbs?.medium?.url ||
          thumbs?.high?.url ||
          thumbs?.default?.url ||
          "";

        return {
          videoId,
          title: decodeHtmlEntities(snippet?.title || ""),
          channel: decodeHtmlEntities(snippet?.channelTitle || ""),
          publishedAt: snippet?.publishedAt || "",
          thumbnailUrl: thumbUrl,
          url: `https://www.youtube.com/watch?v=${videoId}`,
        };
      })
      .filter((item): item is VideoResultItem => item !== null);

    // 7. Simpan 50 hasil lengkap ke search_cache menggunakan adminClient
    await adminClient
      .from("search_cache")
      .upsert({
        cache_key: cacheKey,
        payload: mappedItems,
        created_at: new Date().toISOString(),
      })
      .catch(() => {
        // Caching failure tidak boleh menggagalkan respons ke pengguna
      });

    // 8. Kembalikan hanya sejumlah `limit` pertama dengan mempertahankan urutan relevansi
    const slicedResult = mappedItems.slice(0, limit);

    return new Response(
      JSON.stringify({
        items: slicedResult,
        cached: false,
      }),
      {
        status: 200,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  } catch (error) {
    return new Response(
      JSON.stringify({
        error: "Terjadi kesalahan pada server saat memproses pencarian.",
        detail: (error as Error).message,
      }),
      {
        status: 500,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    );
  }
});

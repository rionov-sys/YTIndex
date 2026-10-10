# Supabase Edge Functions - YT Index

Folder ini berisi Edge Functions berbasis Deno TypeScript yang dideploy ke Supabase untuk menangani logika pencarian YouTube yang aman, terautentikasi, dan hemat kuota.

## Struktur

```
supabase/functions/
├── deno.json
├── README.md
└── youtube-search/
    ├── index.ts     # Edge Function utama: JWT auth, caching, YouTube API & rate limit mapping
    ├── test.sh      # Script testing via curl (Bash)
    └── test.ps1     # Script testing via PowerShell
```

## Menjalankan Secara Lokal

Untuk menguji edge function di komputer lokal:

```bash
# Jalankan Supabase lokal
supabase start

# Jalankan Edge Function secara lokal
supabase functions serve youtube-search --no-verify-jwt --env-file ../../.env.local
```

## Deploy ke Supabase Cloud

```bash
# 1. Atur YouTube Data API Key di secret Supabase Cloud
supabase secrets set YOUTUBE_API_KEY=AIzaSy...

# 2. Deploy function
supabase functions deploy youtube-search
```

> [!WARNING]
> Jangan pernah meng-commit `service_role` key ke dalam file kode atau repositori Git! Simpan token sensitif hanya di environment secrets Supabase Dashboard atau secret CLI.

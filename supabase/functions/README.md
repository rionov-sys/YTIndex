# Supabase Edge Functions - YT Index

Folder ini berisi Edge Functions berbasis Deno TypeScript yang dideploy ke Supabase.

## Struktur

```
supabase/functions/
├── README.md
└── search-indexer/
    └── index.ts     # Edge Function untuk caching hasil pencarian & analitik
```

## Menjalankan Secara Lokal

Untuk menguji edge function di komputer lokal:

```bash
# Pastikan Supabase CLI terpasang (supabase start sudah berjalan)
supabase functions serve search-indexer --no-verify-jwt
```

## Deploy ke Supabase Cloud

```bash
supabase functions deploy search-indexer --project-ref <PROJECT_ID>
```

> [!WARNING]
> Jangan pernah meng-commit `service_role` key ke dalam file kode atau repository! Simpan token sensitif hanya di environment secrets Supabase Dashboard (`supabase secrets set KEY=VALUE`).

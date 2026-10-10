# Supabase Backend Configuration - YT Index

Direktori ini berisi konfigurasi backend Supabase, migrasi database PostgreSQL, dan Edge Functions untuk aplikasi Android **YT Index**.

## Struktur Direktori

```
supabase/
├── config.toml                     # Konfigurasi Supabase CLI
├── migrations/                     # File migrasi database PostgreSQL (skema, RLS, index)
│   └── 20261010000000_init_ytindex_schema.sql
├── functions/                      # Deno Edge Functions
│   ├── README.md
│   └── search-indexer/index.ts
└── README.md                       # Dokumentasi backend ini
```

## Menjalankan Supabase Secara Lokal

Prasyarat: [Docker Desktop](https://www.docker.com/) & [Supabase CLI](https://supabase.com/docs/guides/cli).

```bash
# 1. Jalankan container lokal Supabase
supabase start

# 2. Terapkan migrasi database
supabase db reset

# 3. Buat migrasi baru jika ada perubahan skema
supabase db diff -f nama_perubahan

# 4. Hentikan container lokal
supabase stop
```

## Kebijakan Keamanan & Kunci Rahasia (Security Policy)

1. **SUPABASE_URL & SUPABASE_ANON_KEY**:
   - `anon_key` adalah public API key yang aman dimasukkan ke aplikasi klien (Android).
   - Seluruh tabel database dilindungi oleh **Row Level Security (RLS)** sehingga klien hanya dapat mengakses data sesuai policy yang didefinisikan.
2. **SERVICE_ROLE KEY (DILARANG KERAS DI-COMMIT)**:
   - Kunci `service_role` mengabaikan seluruh aturan RLS (*admin full access*).
   - **JANGAN PERNAH** memasukkan `service_role` ke repositori git, file Android, atau klien aplikasi.
   - Kunci ini hanya digunakan di server internal atau CI secret khusus backend.
3. Kunci dibaca dari:
   - Lingkungan lokal Android: `local.properties` (sudah diabaikan oleh `.gitignore`).
   - GitHub Actions CI: GitHub Repository Secrets (`Settings > Secrets and variables > Actions`).

# Supabase Backend - YT Index 🎬

Direktori ini berisi konfigurasi backend lengkap, skema database PostgreSQL, Row Level Security (RLS), dan Edge Function untuk aplikasi Android **YT Index**. Seluruh konfigurasi disusun agar dapat direproduksi (*reproducible*) kapan saja menggunakan Supabase CLI.

---

## 📁 Struktur Direktori

```
supabase/
├── config.toml                              # Konfigurasi lokal & deklarasi Edge Functions
├── migrations/
│   └── 20261010000000_init_ytindex_schema.sql  # Skema tabel, indeks, & RLS policies
├── functions/
│   ├── deno.json                            # Konfigurasi compiler Deno
│   └── youtube-search/                      # Edge Function utama pencarian YouTube
│       ├── index.ts                         # Kode Deno TypeScript
│       ├── test.sh                          # Script pengujian via curl (Bash)
│       └── test.ps1                         # Script pengujian via PowerShell
└── README.md                                # Dokumentasi panduan teknis ini
```

---

## 🗄️ 1. Skema Database (PostgreSQL)

Skema didefinisikan dalam file [migrations/20261010000000_init_ytindex_schema.sql](file:///d:/DATA%20PRIBADI%20RIO/PROJECT-VIBE%20CODING/PRODUCTION/YTIndex/supabase/migrations/20261010000000_init_ytindex_schema.sql):

### A. Tabel `bookmarks`
Menyimpan video favorit/bookmark pengguna pribadi:
- `id` (`uuid`, Primary Key, `default gen_random_uuid()`)
- `user_id` (`uuid`, Foreign Key ke `auth.users(id)` ON DELETE CASCADE, `default auth.uid()`)
- `video_id` (`text`, NOT NULL)
- `title` (`text`)
- `channel` (`text`)
- `thumbnail_url` (`text`)
- `published_at` (`timestamptz`)
- `url` (`text`)
- `created_at` (`timestamptz`, `default now()`)
- Constraint: `unique(user_id, video_id)`
- Indeks: `idx_bookmarks_user_created_at` pada `(user_id, created_at desc)`

### B. Tabel `search_history`
Menyimpan riwayat kata kunci pencarian yang pernah diketik pengguna:
- `id` (`uuid`, Primary Key, `default gen_random_uuid()`)
- `user_id` (`uuid`, Foreign Key ke `auth.users(id)` ON DELETE CASCADE, `default auth.uid()`)
- `query` (`text`, NOT NULL)
- `result_limit` (`int`)
- `created_at` (`timestamptz`, `default now()`)
- Indeks: `idx_search_history_user_created_at` pada `(user_id, created_at desc)`

### C. Tabel `search_cache`
Menyimpan cache hasil pencarian YouTube API bersama untuk menghemat kuota:
- `cache_key` (`text`, Primary Key) — format: query lowercase trimmed + bucket tanggal UTC (`query_YYYY-MM-DD`)
- `payload` (`jsonb`, NOT NULL) — daftar 50 item video lengkap dari YouTube API
- `created_at` (`timestamptz`, `default now()`)
- Indeks: `idx_search_cache_created_at` pada `(created_at desc)`

---

## 🛡️ 2. Row Level Security (RLS) & Kebijakan Keamanan

Seluruh tabel dilindungi oleh Row Level Security (**RLS Enabled**):

| Tabel | Role yang Diizinkan | Aturan / Kebijakan (Policy) |
|---|---|---|
| `bookmarks` | `authenticated` | `SELECT`, `INSERT`, `DELETE` **hanya** untuk baris di mana `user_id = auth.uid()`. |
| `search_history` | `authenticated` | `SELECT`, `INSERT`, `DELETE` **hanya** untuk baris di mana `user_id = auth.uid()`. |
| `search_cache` | `service_role` (Hanya Edge Function) | **RLS aktif TANPA policy**. Pengguna publik (`anon`) maupun login (`authenticated`) memiliki akses 0 (ditolak total). Hanya Edge Function dengan token `service_role` yang dapat membaca/menulis tabel ini. |

---

## ⚡ 3. Edge Function: `youtube-search`

Fungsi berbasis **Deno + TypeScript** yang menangani pencarian aman, caching, dan pemetaan error:

### Alur Kerja:
1. **Verifikasi Autentikasi**: Memeriksa keberadaan header `Authorization: Bearer <user_jwt>` dan memvalidasi ke Supabase Auth (`verify_jwt = true`).
2. **Validasi Input**:
   - `query`: dipotong spasi (trimmed), wajib 1–100 karakter.
   - `limit`: dibatasi (*clamped*) integer 1–50 (default 10).
3. **Pencatatan Riwayat**: Secara otomatis mencatat kata kunci ke tabel `search_history` milik user terkait.
4. **Cek Cache**:
   - Membuat `cache_key`: `${query.toLowerCase()}_${todayUtc}`.
   - Jika data ditemukan di `search_cache` dan usianya **< 24 jam**, respons diambil langsung dari cache (`cached: true`).
5. **Panggilan YouTube Data API v3 (Cache Miss)**:
   - Dipanggil sekali dengan parameter: `part=snippet`, `type=video`, `order=relevance`, `maxResults=50`, `publishedAfter = 365 hari lalu`, `relevanceLanguage=id`, `regionCode=ID`.
   - Mengambil `YOUTUBE_API_KEY` dari environment secret Supabase.
   - Melakukan decode terhadap seluruh karakter HTML entity pada judul video (`decodeHtmlEntities`).
   - Menyimpan daftar lengkap 50 video ke tabel `search_cache`.
6. **Pemotongan Hasil**: Mengembalikan sejumlah `limit` teratas sesuai urutan relevansi asli dari YouTube (`cached: false`).
7. **Penanganan Error**:
   - Error 403 `quotaExceeded` / `dailyLimitExceeded` dari YouTube secara otomatis dipetakan ke **HTTP 429** dengan pesan ramah dalam Bahasa Indonesia:
     `"Batas kuota harian YouTube Data API telah tercapai (quotaExceeded). Silakan coba lagi besok atau gunakan video yang telah tersimpan."`
   - Kunci API YouTube maupun detail error upstream internal **tidak pernah dibocorkan** ke klien.

---

## 👤 4. Autentikasi Pengguna & Penonaktifan Sign-Up

Aplikasi ini ditujukan sebagai **Personal App** (aplikasi pribadi):

1. **Registrasi Akun Pertama Kali**:
   - Buat akun Anda melalui fitur Sign Up aplikasi atau via Dashboard Supabase (**Authentication > Users > Add User**).
2. **Matikan Registrasi Publik (Disable Signups)**:
   - Setelah akun Anda berhasil dibuat, masuk ke **Dashboard Supabase**.
   - Buka menu **Authentication > Providers > Email**.
   - **Hapus centang / Matikan** opsi **"Allow new users to sign up"** (atau toggle **Enable Signup** ke posisi OFF).
   - Klik **Save**.
   - Dengan langkah ini, tidak ada orang asing yang bisa membuat akun baru di backend Anda.

---

## 🚀 5. Perintah Deployment & Secrets (Langkah Demi Langkah)

Jalankan perintah berikut di terminal Anda untuk menghubungkan dan mendeploy backend:

```bash
# 1. Login ke akun Supabase Anda (jika belum)
supabase login

# 2. Hubungkan repositori lokal ke project Supabase Cloud Anda
# Ganti <PROJECT_REF> dengan Reference ID dari dashboard project Anda (contoh: abcdefghijklm)
supabase link --project-ref <PROJECT_REF>

# 3. Terapkan migrasi skema tabel, indeks, dan RLS ke database Cloud
supabase db push

# 4. Setel YouTube Data API Key ke environment secret Supabase Edge Functions
# (Jangan pernah menaruh key ini di git atau di aplikasi Android)
supabase secrets set YOUTUBE_API_KEY=AIzaSy...IsiApiKeyYouTubeAnda

# 5. Deploy Edge Function youtube-search
supabase functions deploy youtube-search
```

> [!IMPORTANT]
> **Pemisahan Kunci Rahasia**:
> - Aplikasi Android **HANYA** memerlukan `SUPABASE_URL` dan `SUPABASE_ANON_KEY` (public anon key).
> - Kunci `SUPABASE_SERVICE_ROLE_KEY` dan `YOUTUBE_API_KEY` dikelola langsung oleh Supabase Edge Function di sisi server dan **DILARANG KERAS** dimasukkan ke dalam kode Android atau commit Git.

---

## 🧪 6. Cara Menguji Edge Function dengan `curl`

### Langkah 1: Dapatkan Access Token (JWT) Pengguna
Lakukan login menggunakan email dan password akun Supabase Anda:

```bash
curl -X POST "https://<PROJECT_REF>.supabase.co/auth/v1/token?grant_type=password" \
  -H "apikey: <SUPABASE_ANON_KEY>" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "email_anda@example.com",
    "password": "password_anda"
  }'
```
Salin nilai `access_token` dari respons JSON di atas.

### Langkah 2: Panggil Edge Function `youtube-search`
Gunakan `access_token` pada header `Authorization`:

```bash
curl -X POST "https://<PROJECT_REF>.supabase.co/functions/v1/youtube-search" \
  -H "Authorization: Bearer <ACCESS_TOKEN_DARI_LANGKAH_1>" \
  -H "apikey: <SUPABASE_ANON_KEY>" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "tutorial jetpack compose indonesia",
    "limit": 5
  }'
```

### Contoh Respons Sukses:
```json
{
  "items": [
    {
      "videoId": "abc123xyz",
      "title": "Tutorial Jetpack Compose untuk Pemula",
      "channel": "Kanal Koding",
      "publishedAt": "2026-05-10T12:00:00Z",
      "thumbnailUrl": "https://i.ytimg.com/vi/abc123xyz/mqdefault.jpg",
      "url": "https://www.youtube.com/watch?v=abc123xyz"
    }
  ],
  "cached": false
}
```

*Jika Anda mengulangi request dengan kata kunci yang sama di hari yang sama, `cached` akan bernilai `true` dan data langsung dikembalikan dari `search_cache` tanpa mengonsumsi kuota YouTube API!*

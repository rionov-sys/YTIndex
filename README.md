# YT Index 🎬

Aplikasi Android modern berbasis **Kotlin** dan **Jetpack Compose** untuk mencari dan mengindeks video YouTube terkini secara cepat, efisien, dan terarah.

---

## 📌 Apa yang Dilakukan Aplikasi Ini?

YT Index memudahkan pengguna menemukan konten video YouTube berkualitas dengan kriteria spesifik:
- **Autentikasi Akun (Supabase Auth)**: Pengguna dapat mendaftar dan masuk menggunakan email/password, dengan sesi login yang tersimpan aman secara otomatis.
- **Pencarian Aman via Supabase Edge Function**: Aplikasi Android tidak memanggil langsung YouTube API. Pemanggilan dilakukan melalui Edge Function `youtube-search` menggunakan token sesi JWT pengguna.
- **Cache Server-Side 24 Jam**: Hasil pencarian di-cache di tabel `search_cache` Supabase selama 24 jam untuk menghemat kuota YouTube Data API v3.
- **Penanganan Kuota Cerdas**: Jika kuota harian YouTube habis (HTTP 429), aplikasi menampilkan pesan informatif dalam Bahasa Indonesia.
- **4 Tab Navigasi Bawah (Bottom Navigation)**:
  1. **Cari**: Input kata kunci, slider jumlah hasil (1–50 video), badge peringkat relevansi (#1, #2...), thumbnail 16:9, judul terdekode, nama channel, tanggal rilis, dan tombol bookmark cepat.
  2. **Tersimpan**: Daftar video favorit yang tersimpan di tabel PostgreSQL `bookmarks` (RLS terisolasi per pengguna).
  3. **Riwayat**: Daftar riwayat pencarian dari tabel `search_history`, kemampuan mencari ulang dengan satu ketukan, dan tombol hapus semua riwayat dengan dialog konfirmasi.
  4. **Pengaturan**: Slider jumlah hasil default pencarian (tersimpan di DataStore), preferensi tema (Ikuti Sistem / Mode Terang / Mode Gelap), informasi email akun aktif, dan tombol keluar akun (logout).
- **Integrasi Langsung**: Ketuk kartu video mana pun untuk langsung membukanya di aplikasi YouTube atau peramban bawaan.

---

## 📁 Struktur Direktori Proyek

Proyek ini disusun dengan struktur yang rapi dan modular:

```
YTIndex/
├── app/                            # Modul aplikasi Android (Kotlin + Jetpack Compose)
│   ├── src/main/AndroidManifest.xml
│   ├── src/main/java/com/example/ytindex/
│   │   ├── MainActivity.kt         # Entry point aplikasi & pengamat tema dinamis
│   │   ├── YTIndexApp.kt           # Application class
│   │   ├── data/
│   │   │   ├── Models.kt           # Model DTO & UI
│   │   │   ├── local/              # DataStore Preferences (tema & limit default)
│   │   │   ├── remote/             # SupabaseClientProvider
│   │   │   └── repository/         # Auth, Search, Bookmark, History Repository
│   │   └── ui/
│   │       ├── MainScreen.kt       # Scaffold dengan 4-tab bottom navigation
│   │       ├── auth/               # Layar & ViewModel Autentikasi (Login/Register)
│   │       ├── search/             # Layar & ViewModel Pencarian
│   │       ├── bookmarks/          # Layar & ViewModel Video Tersimpan
│   │       ├── history/            # Layar & ViewModel Riwayat Pencarian
│   │       ├── settings/           # Layar & ViewModel Pengaturan Tema & Akun
│   │       ├── components/         # VideoCard, BottomNavBar, StateIndicators
│   │       ├── navigation/         # NavHost & Auth Guard
│   │       └── theme/              # Color, Type, Theme (Light/Dark/System)
│   └── build.gradle.kts            # Konfigurasi dependensi modul app
├── supabase/                       # Konfigurasi backend Supabase
│   ├── config.toml                 # Konfigurasi Supabase CLI
│   ├── migrations/                 # Migrasi skema database PostgreSQL & RLS
│   │   └── 20261010000000_init_ytindex_schema.sql
│   ├── functions/                  # Deno Edge Functions
│   │   └── youtube-search/         # Edge function pencarian YouTube & caching
│   └── README.md                   # Panduan teknis Supabase & pengujian
├── design/                         # Referensi desain UI/UX yang diekspor dari Stitch
├── docs/                           # Dokumentasi teknis proyek
├── .github/
│   └── workflows/
│       └── android.yml             # Alur kerja CI/CD GitHub Actions
├── .gitignore                      # Mengabaikan file sensitif, build output, & IDE
├── local.properties.example        # Template konfigurasi kunci rahasia lokal
├── build.gradle.kts                # Root build file
├── settings.gradle.kts             # Pengaturan modul Gradle
└── README.md                       # Dokumentasi utama (file ini)
```

---

## 🌿 Strategi Percabangan & Standar Commit

### Cabang Git (Branches)
- **`main`**: Cabang utama yang stabil (*production-ready*).
- **`feature/*`**: Cabang kerja untuk setiap fitur baru (contoh: `feature/supabase-architecture-and-deps`).

### Format Pesan Commit (Conventional Commits)
Gunakan format standar berikut pada setiap commit:
- `feat:` Penambahan fitur baru (contoh: `feat: implement Supabase auth and edge function search`)
- `fix:` Perbaikan bug (contoh: `fix: add gradle.properties with AndroidX enabled`)
- `chore:` Pemeliharaan, dependensi, konfigurasi build/CI
- `docs:` Pembaruan atau penambahan dokumentasi

---

## 🔐 Manajemen Kunci Rahasia (Secrets Management)

Kunci rahasia dan konfigurasi lingkungan **HANYA** dibaca dari dua sumber:
1. **Lokal**: File `local.properties` di root direktori proyek.
2. **CI / GitHub Actions**: Repository Secrets (`Settings > Secrets and variables > Actions`).

### Daftar Kunci yang Dibutuhkan Aplikasi Android:

| Variabel | Deskripsi | Diperlukan Di |
|---|---|---|
| `SUPABASE_URL` | Endpoint URL project Supabase Anda (`https://<project-id>.supabase.co`) | `local.properties` & GitHub Secrets |
| `SUPABASE_ANON_KEY` | Public Anon Key dari dashboard Supabase untuk autentikasi klien aman | `local.properties` & GitHub Secrets |

> [!NOTE]
> `YOUTUBE_API_KEY` **TIDAK** lagi disimpan pada aplikasi Android. Kunci ini hanya dikonfigurasikan di sisi backend Supabase Edge Function (`supabase secrets set YOUTUBE_API_KEY=...`), menjaga API key sepenuhnya terlindungi dari ekstraksi APK.

> [!CAUTION]
> **ATURAN KEAMANAN WAJIB**:
> - Jangan pernah menaruh nilai asli kunci rahasia di dalam file yang di-commit ke Git.
> - Kunci Supabase `service_role` **DILARANG KERAS** dimasukkan ke dalam repositori maupun aplikasi Android.
> - File `local.properties`, `*.jks`, `*.keystore`, `.env*`, `.idea`, dan seluruh build output sudah diblokir secara otomatis oleh `.gitignore`.

---

## 🚀 Panduan Setup & Menjalankan Aplikasi

### 1. Konfigurasi `local.properties`
Salin template dan isi dengan kredensial Supabase Anda:
```bash
cp local.properties.example local.properties
```
Buka file `local.properties` dan lengkapi:
```properties
sdk.dir=/path/ke/Android/Sdk
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_ANON_KEY=your_supabase_anon_public_key
```

### 2. Menjalankan di Android Studio
1. Buka folder proyek ini di **Android Studio**.
2. Biarkan Gradle melakukan sinkronisasi otomatis (*Gradle Sync*).
3. Pilih emulator Android atau perangkat fisik (USB Debugging).
4. Tekan tombol **Run (Shift + F10)**.

### 3. Menjalankan via Terminal (CLI)
```bash
# Menjalankan unit tests
./gradlew test

# Kompilasi APK debug
./gradlew assembleDebug

# Menjalankan pemeriksaan Lint
./gradlew lint

# File APK yang dihasilkan:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ Alur CI/CD (GitHub Actions)

Repositori ini telah dilengkapi pipeline otomatis di `.github/workflows/android.yml`:
- **Pemicu**: Setiap ada `push` atau `pull_request` yang menargetkan cabang `main`.
- **Lingkungan**: Ubuntu Latest dengan **JDK 17** (Temurin).
- **Gradle Caching**: Caching dependensi dan build cache otomatis menggunakan `gradle/actions/setup-gradle@v4`.
- **Verifikasi**: Menjalankan `./gradlew lint` dan `./gradlew assembleDebug`.
- **Artefak**: Mengunggah file `app-debug.apk` sebagai build artifact yang dapat langsung diunduh dari halaman GitHub Actions.

---

## 📄 Lisensi

Proyek ini dikembangkan secara internal untuk kebutuhan produktivitas dan indeks video. Hak cipta dilindungi undang-undang.

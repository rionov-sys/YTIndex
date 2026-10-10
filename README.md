# YT Index 🎬

Aplikasi Android modern berbasis **Kotlin** dan **Jetpack Compose** untuk mencari dan mengindeks video YouTube terkini secara cepat, efisien, dan terarah.

---

## 📌 Apa yang Dilakukan Aplikasi Ini?

YT Index memudahkan pengguna menemukan konten video YouTube berkualitas dengan kriteria spesifik:
- **Filter 1 Tahun Terakhir**: Hanya menampilkan video yang diunggah dalam kurun waktu 365 hari terakhir (`publishedAfter`), memastikan hasil pencarian selalu relevan dan mutakhir.
- **Urutan Paling Relevan**: Mempertahankan pemeringkatan relevansi resmi dari YouTube Data API v3.
- **Batas Hasil Fleksibel**: Jumlah hasil pencarian dapat disesuaikan secara dinamis oleh pengguna melalui slider (1–50 video, default 10 video).
- **Antarmuka Bersih & Responsif**: Kartu video menampilkan thumbnail rasio 16:9, judul video (entitas HTML telah didekode), nama channel pembuat, dan tanggal publikasi.
- **Integrasi Langsung**: Ketuk kartu video mana pun untuk langsung membukanya di aplikasi YouTube atau peramban bawaan.
- **Dukungan Backend Supabase**: Skema database PostgreSQL siap pakai untuk menyimpan riwayat pencarian (*search history*) dan video favorit (*bookmark*), dilengkapi Row Level Security (RLS) serta Deno Edge Functions.

---

## 📁 Struktur Direktori Proyek

Proyek ini disusun dengan struktur yang rapi dan modular:

```
YTIndex/
├── app/                            # Modul aplikasi Android (Kotlin + Jetpack Compose)
│   ├── src/main/AndroidManifest.xml
│   ├── src/main/java/com/example/ytindex/
│   │   ├── MainActivity.kt         # Entry point aplikasi & tema Material 3
│   │   ├── data/
│   │   │   ├── Models.kt           # Data class respons API & model domain UI
│   │   │   ├── YouTubeApi.kt       # Retrofit service interface YouTube Data API v3
│   │   │   └── VideoRepository.kt  # Logika filter 1 tahun, relevansi, dan kuota
│   │   └── ui/
│   │       ├── SearchScreen.kt     # Tampilan Compose (Search bar, slider, list)
│   │       └── SearchViewModel.kt  # Pengelolaan state (StateFlow) & error handling
│   └── build.gradle.kts            # Konfigurasi dependensi modul app
├── supabase/                       # Konfigurasi backend Supabase
│   ├── config.toml                 # Konfigurasi Supabase CLI
│   ├── migrations/                 # Migrasi skema database PostgreSQL & RLS
│   │   └── 20261010000000_init_ytindex_schema.sql
│   ├── functions/                  # Deno Edge Functions
│   │   └── search-indexer/index.ts
│   └── README.md                   # Panduan teknis Supabase
├── design/                         # Referensi desain UI/UX yang diekspor dari Stitch
│   ├── screens/                    # Mockup dan tangkapan layar UI
│   ├── tokens/                     # Design tokens (warna, font, spacing JSON)
│   │   └── design_tokens.json
│   ├── specs/                      # Spesifikasi interaksi UI
│   └── README.md                   # Dokumentasi panduan desain
├── docs/                           # Dokumentasi teknis proyek
│   ├── ARCHITECTURE.md             # Arsitektur detail MVVM & Compose
│   ├── SETUP.md                    # Panduan setup developer & rahasia
│   └── WORKFLOW.md                 # Alur branching Git & Conventional Commits
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
- **`feature/*`**: Cabang kerja untuk setiap fitur baru (contoh: `feature/supabase-auth`, `feature/offline-cache`, `feature/video-player`).

### Format Pesan Commit (Conventional Commits)
Gunakan format standar berikut pada setiap commit:
- `feat:` Penambahan fitur baru (contoh: `feat: tambahkan penyimpanan bookmark ke Supabase`)
- `fix:` Perbaikan bug (contoh: `fix: tangani error kuota 403 YouTube API`)
- `chore:` Pemeliharaan, dependensi, konfigurasi build/CI (contoh: `chore: initial project setup`)
- `docs:` Pembaruan atau penambahan dokumentasi (contoh: `docs: perbarui panduan setup`)

---

## 🔐 Manajemen Kunci Rahasia (Secrets Management)

Kunci rahasia dan konfigurasi lingkungan **HANYA** dibaca dari dua sumber:
1. **Lokal**: File `local.properties` di root direktori proyek.
2. **CI / GitHub Actions**: Repository Secrets (`Settings > Secrets and variables > Actions`).

### Daftar Kunci yang Dibutuhkan:

| Variabel | Deskripsi | Diperlukan Di |
|---|---|---|
| `YOUTUBE_API_KEY` | API Key dari Google Cloud Console dengan hak akses YouTube Data API v3 | `local.properties` & GitHub Secrets |
| `SUPABASE_URL` | Endpoint URL project Supabase Anda (`https://<project-id>.supabase.co`) | `local.properties` & GitHub Secrets |
| `SUPABASE_ANON_KEY` | Public Anon Key dari dashboard Supabase untuk autentikasi klien aman | `local.properties` & GitHub Secrets |

> [!CAUTION]
> **ATURAN KEAMANAN WAJIB**:
> - Jangan pernah menaruh nilai asli kunci rahasia di dalam file yang di-commit ke Git.
> - Kunci Supabase `service_role` **DILARANG KERAS** dimasukkan ke dalam repositori maupun aplikasi Android.
> - File `local.properties`, `*.jks`, `*.keystore`, `.env*`, `.idea`, dan seluruh build output sudah diblokir secara otomatis oleh `.gitignore`.

---

## 🚀 Panduan Setup & Menjalankan Aplikasi

### 1. Dapatkan YouTube API Key
1. Masuk ke [Google Cloud Console](https://console.cloud.google.com).
2. Buat project baru atau pilih project yang ada.
3. Buka **APIs & Services > Library**, cari dan aktifkan **YouTube Data API v3**.
4. Buka **APIs & Services > Credentials**, pilih **Create Credentials > API key**.
5. *(Disarankan)* Berikan restriction ke package Android: `com.example.ytindex` beserta fingerprint SHA-1 debug sertifikat Anda.

### 2. Konfigurasi `local.properties`
Salin template dan isi dengan kredensial Anda:
```bash
cp local.properties.example local.properties
```
Buka file `local.properties` dan lengkapi:
```properties
sdk.dir=/path/ke/Android/Sdk
YOUTUBE_API_KEY=AIzaSy...
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_ANON_KEY=your_supabase_anon_public_key
```

### 3. Menjalankan di Android Studio
1. Buka folder proyek ini di **Android Studio**.
2. Biarkan Gradle melakukan sinkronisasi otomatis (*Gradle Sync*).
3. Pilih emulator Android atau perangkat fisik (USB Debugging).
4. Tekan tombol **Run (Shift + F10)**.

### 4. Menjalankan via Terminal (CLI)
```bash
# Kompilasi APK debug
./gradlew assembleDebug

# Menjalankan pengujian Lint
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

# Panduan Setup & Konfigurasi - YT Index

Panduan ini berisi langkah-langkah lengkap untuk menyiapkan lingkungan pengembangan lokal dan CI/CD.

---

## 1. Prasyarat Lingkungan

- **JDK 17** (Temurin, Zulu, atau Android Studio embedded JBR)
- **Android Studio** (Koala / Ladybug atau versi terbaru)
- **Android SDK**: `compileSdk 35`, `minSdk 26`, `targetSdk 35`
- **Git** (versi 2.30+)

---

## 2. Manajemen Kunci Rahasia (Secrets Management)

Aplikasi membaca kredensial melalui:
1. **Lokal**: `local.properties` di root folder proyek (diabaikan oleh git).
2. **CI / GitHub Actions**: Environment variables yang disuntikkan dari GitHub Actions Secrets.

### Kunci yang Diperlukan:

| Nama Kunci | Deskripsi | Sumber |
|---|---|---|
| `YOUTUBE_API_KEY` | API Key Google Cloud untuk YouTube Data API v3 | [Google Cloud Console](https://console.cloud.google.com) |
| `SUPABASE_URL` | URL endpoint proyek Supabase | [Supabase Dashboard](https://supabase.com/dashboard) |
| `SUPABASE_ANON_KEY` | Public anon key untuk akses Supabase | [Supabase Dashboard](https://supabase.com/dashboard) |

> [!CAUTION]
> **PERINGATAN KEAMANAN**:
> - JANGAN PERNAH memasukkan kunci rahasia ke dalam file kode Kotlin, file XML, atau commit git.
> - Kunci Supabase `service_role` TIDAK BOLEH dimasukkan ke dalam repo maupun aplikasi Android.

---

## 3. Langkah Konfigurasi Lokal

1. Salin template `local.properties.example` menjadi `local.properties`:
   ```bash
   cp local.properties.example local.properties
   ```
2. Isi nilai yang sebenarnya di `local.properties`:
   ```properties
   sdk.dir=C:\\Users\\NAMA_USER\\AppData\\Local\\Android\\Sdk
   YOUTUBE_API_KEY=AIzaSy...
   SUPABASE_URL=https://xyzcompany.supabase.co
   SUPABASE_ANON_KEY=eyJhbGciOi...
   ```
3. Buka proyek di Android Studio, tunggu Gradle Sync selesai.

---

## 4. Konfigurasi GitHub Actions Secrets

Untuk menjalankan alur CI/CD otomatis di GitHub:
1. Buka repositori di GitHub.
2. Navigasikan ke **Settings > Secrets and variables > Actions**.
3. Klik **New repository secret** dan tambahkan:
   - `SUPABASE_URL`
   - `SUPABASE_ANON_KEY`
   - `YOUTUBE_API_KEY`

---

## 5. Menjalankan Build dari Terminal

```bash
# Build APK Debug
./gradlew assembleDebug

# Jalankan Android Lint
./gradlew lint

# Jalankan Unit Test
./gradlew test
```
File APK hasil build akan berada di: `app/build/outputs/apk/debug/app-debug.apk`.

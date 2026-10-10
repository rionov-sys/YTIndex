# Alur Kerja Git & Standar Kolaborasi - YT Index

Dokumen ini mendefinisikan strategi percabangan (*branching strategy*), konvensi commit (*Conventional Commits*), dan panduan alur kerja tim untuk proyek **YT Index**.

---

## 1. Strategi Percabangan (Branching Strategy)

Model percabangan yang digunakan adalah model *Feature Branching*:

- **`main`**:
  - Cabang utama yang selalu berada dalam kondisi stabil (*production-ready*).
  - Tidak diperkenankan melakukan push langsung (*direct push*) ke `main` di lingkungan tim produksi.
  - Setiap perubahan harus melalui Pull Request (PR) yang telah lolos pengujian alur CI (GitHub Actions).
- **`feature/*`**:
  - Cabang kerja untuk pengembangan setiap fitur baru.
  - Format penamaan cabang:
    - `feature/search-history`
    - `feature/bookmark-video`
    - `feature/supabase-integration`
    - `feature/offline-cache`
- **`fix/*`** *(opsional)*:
  - Cabang untuk perbaikan bug tertentu, misal: `fix/html-entity-decoding`.

---

## 2. Standar Commit (Conventional Commits)

Format penulisan pesan commit:
```
<type>(<scope>): <deskripsi singkat>

[opsional: deskripsi lebih panjang]
```

Tipe commit yang digunakan:
- **`feat:`** Penambahan fitur baru ke aplikasi (misal: `feat: tambahkan fitur simpan bookmark video`).
- **`fix:`** Perbaikan bug atau kesalahan logika (misal: `fix: tangani error batas kuota 403 YouTube API`).
- **`chore:`** Perubahan konfigurasi proyek, build script, dependensi, atau CI/CD (misal: `chore: initial project setup`).
- **`docs:`** Penambahan atau pembaruan dokumentasi (misal: `docs: perbarui panduan setup secrets`).

Contoh commit yang baik:
```bash
git commit -m "feat(ui): tambahkan slider kustom batas hasil pencarian"
git commit -m "fix(api): decode entitas HTML pada judul video"
git commit -m "chore(ci): tambahkan alur kerja GitHub Actions untuk assembleDebug"
git commit -m "docs: tambahkan dokumentasi skema database Supabase"
```

---

## 3. Siklus Pengembangan Fitur (Feature Lifecycle)

1. **Buat Branch Baru dari `main`**:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b feature/nama-fitur
   ```
2. **Kembangkan & Commit**:
   ```bash
   git add .
   git commit -m "feat: implementasi fungsi pencarian"
   ```
3. **Validasi Lokal Sebelum Push**:
   ```bash
   ./gradlew lint
   ./gradlew assembleDebug
   ```
4. **Push Branch ke GitHub**:
   ```bash
   git push -u origin feature/nama-fitur
   ```
5. **Buat Pull Request (PR) ke `main`**:
   - Pastikan GitHub Actions CI berhasil (*All checks have passed*).
   - Review kode sebelum melakukan merge (Squash and merge direkomendasikan).

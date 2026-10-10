# Design Assets & References - Stitch (YT Index)

Folder ini menyimpan seluruh referensi UI/UX, desain antarmuka, token desain, dan aset visual yang diekspor dari **Stitch**.

## Struktur Direktori

```
design/
├── README.md           # Panduan dan deskripsi desain ini
├── screens/            # Mockup layar, wireframe, dan tangkapan layar UI
│   └── .gitkeep
├── tokens/             # Design tokens (warna, tipografi, spacing, radius dalam format JSON/MD)
│   └── design_tokens.json
└── specs/              # Spesifikasi interaksi dan panduan komponen
    └── .gitkeep
```

## Ringkasan Desain (UI Overview)

- **Platform**: Android Native (Jetpack Compose, Material 3)
- **Tema**: Adaptif (Light & Dark theme)
- **Komponen Utama**:
  - `TopAppBar`: Header aplikasi bertuliskan "YT Index"
  - `OutlinedTextField`: Input pencarian dengan aksi keyboard Search (IME Action)
  - `Slider & Chip`: Pengaturan batas hasil pencarian (1–50, default 10)
  - `Button`: Tombol "Cari Video" dengan status loading (CircularProgressIndicator)
  - `VideoCard`: Kartu hasil pencarian memuat thumbnail rasio 16:9, judul video (HTML entity decoded), nama channel, tanggal publikasi, dan handler klik untuk langsung membuka video di aplikasi YouTube / Browser.

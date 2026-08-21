# AduAja

**AduAja** adalah platform pengaduan masyarakat berbasis web yang memungkinkan warga melaporkan masalah infrastruktur, kebersihan, dan fasilitas umum. Laporan diverifikasi oleh Admin Pusat, diteruskan ke dinas terkait, lalu ditindaklanjuti oleh petugas lapangan — semua dalam satu alur kerja yang transparan dan terukur.

---

## Kontributor

**Cristoval Pratama Siahaan**

---

## Fitur Utama

- **Warga** — Buat laporan dengan foto + GPS, pantau status, konfirmasi atau ajukan sengketa
- **Admin Pusat** — Validasi, disposisi ke dinas, merge laporan duplikat, monitor SLA
- **Admin Dinas** — Assign petugas, kelola progress, jeda/resume SLA, tangani sengketa, reassign tugas
- **Petugas Lapangan** — Check-in/out GPS, terima tugas, selesaikan dengan foto before-after (watermark otomatis), ajukan penundaan
- **API REST** — Endpoint lengkap untuk konsumsi aplikasi mobile (`/api/**`, autentikasi JWT)

---

## Tech Stack

| Komponen | Teknologi |
|---|---|
| Backend | Java 25, Spring Boot 4.0.6, Maven |
| Database | PostgreSQL (default) / H2 (fallback untuk dev) |
| Storage Gambar | PostgreSQL (default) / Supabase (opsional) |
| Frontend | Thymeleaf, Tailwind CSS, Leaflet.js |
| Auth | Spring Security — Session untuk web, JWT untuk API mobile |
| Email | Brevo HTTP API |
| Deploy | Docker, Fly.io |

---

## Cara Menjalankan

### Prasyarat

- JDK 25+
- PostgreSQL lokal (opsional — H2 juga didukung untuk mode dev cepat)

### Langkah

1. **Clone repositori**
   ```
   git clone <url-repositori-kamu>
   cd AduAja
   ```
2. **Siapkan environment**
   ```
   copy .env.example .env   # Windows
   # cp .env.example .env   # Linux/macOS
   ```
   Isi nilai pada `.env` sesuai kebutuhan. Default sudah bisa jalan dengan H2.

3. **Jalankan**
   ```
   mvnw spring-boot:run
   ```
4. Buka **http://localhost:8081** (ubah dengan env `PORT` bila perlu).

> Konfigurasi database diatur lewat `DB_TYPE` (`postgres` atau `h2`). Panduan lengkap, daftar akun demo, dan skenario uji ada di [`TUTORIAL_RUN.md`](TUTORIAL_RUN.md).

---

## Keamanan

- **Tidak ada secret yang di-commit.** Semua kredensial (DB, OAuth, JWT, email, storage) dibaca dari environment / `.env`.
- **JWT signing key** dibaca dari `JWT_SECRET`. Jika tidak di-set, key acak dibuat saat startup (token hanya berlaku selama satu proses berjalan).
- File `.env` dan artefak build (`target/`, `data/`, dll.) tidak dilacak git — lihat [`.gitignore`](.gitignore).

---

## Struktur Proyek

```
src/main/java/com/plr/aduaja/
├── config/          # Security, env loading, database recovery, data seeder
├── controller/      # Web controller (Thymeleaf) & REST API
├── dto/             # Data Transfer Objects
├── model/           # Entitas JPA
├── repository/      # Spring Data JPA repository
├── security/        # JWT util, filter autentikasi
├── service/         # Logika bisnis
└── storage/         # StorageService (PostgreSQL / Supabase)
src/main/resources/
├── templates/       # View Thymeleaf (admin, warga, petugas)
└── application*.properties
postman/             # Koleksi Postman AduAja API
docs/                # Dokumentasi (API, akun demo, panduan menjalankan)
```

---

## Lisensi

Proyek ini dibuat untuk keperluan tugas mata kuliah **Analisis dan Desain Perangkat Lunak**.

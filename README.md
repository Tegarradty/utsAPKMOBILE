# ☕ Kopi dan Nyemil

**Kopi dan Nyemil** adalah aplikasi Android sederhana untuk memesan kopi dan camilan secara online. Aplikasi ini dibuat sebagai tugas mata kuliah **Pemrograman Mobile**, berdasarkan wireframe *"Kopi dan Nyemil"* (Splash, Sign Up, Login/Register, Home, Profile/Order, Alamat, Konfirmasi).

Aplikasi tidak memakai database. Semua data (akun, menu, keranjang, pesanan) memakai **data statis (dummy)** yang disimpan di memori selama aplikasi berjalan.

> 👤 **Identitas**
> Nama: Tegar Raditya Islamifasha
> NIM: 1247050076
> Program Studi: Teknik Informatika, UIN Sunan Gunung Djati Bandung

---

## ✨ Fitur

| No | Halaman | Fitur |
|----|---------|-------|
| 1 | **Splash / Welcome** | Logo, nama aplikasi, tagline *"Get a taste of your wish"*, tombol **Start now** |
| 2 | **Sign Up** | Penjelasan singkat aplikasi, tombol **Register** dan **Login** |
| 3 | **Login User** | Input username & password, validasi input, tampil/sembunyikan password, info akun demo |
| 4 | **Register** | Input nama lengkap, username, password, validasi (username unik, password minimal 6 karakter), langsung masuk ke Home setelah daftar |
| 5 | **Home** | Sapaan *"Halo Nama,"*, banner, filter kategori (Semua / Kopi / Non-Kopi / Makanan), **9 menu** makanan & minuman, tombol tambah dan pengatur jumlah (− / +), bar keranjang berisi jumlah item & subtotal |
| 6 | **Profile** | Kartu profil (inisial, nama, username), **Profile Setting** untuk mengubah nama, tombol **Save** dan **Logout** |
| 7 | **Alamat Pengiriman (Order)** | Ringkasan pesanan (item, subtotal, ongkir, total), form Nama Lengkap, Alamat, Patokan, validasi, tombol **Order dan Kirim** |
| 8 | **Konfirmasi Pesanan** | Ikon sukses, pesan terima kasih, nomor pesanan, daftar item, alamat tujuan, estimasi tiba, total bayar, tombol **Oke** kembali ke Home |

Fitur tambahan:
- **Bottom navigation** (Home, Order, Profile) sesuai wireframe.
- Keranjang belanja: jumlah item bisa ditambah atau dikurangi langsung di daftar menu.
- Nama yang diubah di Profile langsung tampil di Home, Alamat, dan Konfirmasi.
- Tampilan modern bertema kopi (warna espresso, karamel, krem), sudut membulat, dan Material Design 3.

### Daftar menu (dummy data)

| Menu | Kategori | Harga |
|------|----------|-------|
| Kopi Susu Gula Aren | Kopi | Rp 22.000 |
| Cappuccino | Kopi | Rp 25.000 |
| Americano | Kopi | Rp 20.000 |
| Matcha Latte | Non-Kopi | Rp 27.000 |
| Chocolate Signature | Non-Kopi | Rp 24.000 |
| Donat Cokelat | Makanan | Rp 12.000 |
| Sourdough Loaf | Makanan | Rp 35.000 |
| Croissant Butter | Makanan | Rp 18.000 |
| Pisang Goreng Keju | Makanan | Rp 15.000 |

Ongkos kirim tetap: **Rp 8.000**.

---

## 🔄 Alur Aplikasi

```
Splash/Welcome ──Start now──▶ Sign Up ──Register──▶ Register ──▶ Home
                                   └────Login─────▶ Login ─────▶ Home

Home ──(pilih menu)──▶ Order ──▶ Alamat Pengiriman ──Order dan Kirim──▶ Konfirmasi ──Oke──▶ Home
Home ◀──bottom nav──▶ Profile ──Save──▶ (nama diperbarui)
```

**Akun demo:** username `tegar`, password `123456` (atau buat akun baru lewat Register).

---

## 🛠️ Teknologi yang Digunakan

- **Bahasa:** Kotlin
- **UI:** XML Layout + Material Components (Material Design 3)
- **IDE:** Android Studio
- **Build system:** Gradle (Kotlin DSL) dengan Version Catalog (`libs.versions.toml`)
- **Android Gradle Plugin:** 9.4.1
- **Min SDK:** 27 (Android 8.1) · **Target/Compile SDK:** 37
- **Library:** AndroidX AppCompat, Core KTX, Activity KTX, ConstraintLayout, RecyclerView, Material Components
- **Penyimpanan data:** data statis di memori (`AppData.kt`), tanpa database

---

## 📁 Struktur Project

```
app/src/main/
├── java/lat/pam/kopidannyemil/
│   ├── AppData.kt            # Model + data dummy (user, menu, keranjang, pesanan)
│   ├── UiHelpers.kt          # Helper edge-to-edge, navigasi, keyboard
│   ├── WelcomeActivity.kt    # 1. Splash / Welcome
│   ├── SignUpActivity.kt     # 2. Sign up
│   ├── LoginActivity.kt      # 3. Login
│   ├── RegisterActivity.kt   # 4. Register
│   ├── MainActivity.kt       # 5. Home + 6. Profile (bottom navigation)
│   ├── MenuAdapter.kt        # Adapter RecyclerView daftar menu
│   ├── AlamatActivity.kt     # 7. Alamat pengiriman (Order)
│   └── KonfirmasiActivity.kt # 8. Konfirmasi pesanan
└── res/
    ├── layout/               # Tampilan XML setiap halaman
    ├── drawable/             # Ikon vector & background
    ├── menu/                 # Menu bottom navigation
    └── values/               # Warna, tema, style
```

---

## ▶️ Cara Menjalankan Aplikasi

1. Clone repository ini:
   ```bash
   git clone https://github.com/<username-github>/KopiDanNyemil.git
   ```
2. Buka **Android Studio** → **File › Open** → pilih folder `KopiDanNyemil`.
3. Tunggu proses **Gradle Sync** selesai.
4. Jalankan emulator (AVD) atau sambungkan HP Android dengan **USB debugging** aktif.
5. Klik tombol **Run ▶** (Shift + F10).
6. Untuk membuat file APK: **Build › Build App Bundle(s) / APK(s) › Build APK(s)**. File APK ada di `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📸 Screenshot

| Splash / Welcome | Sign Up | Login | Register |
|:---:|:---:|:---:|:---:|
| ![Splash](screenshots/1_splash.png) | ![Sign Up](screenshots/2_signup.png) | ![Login](screenshots/3_login.png) | ![Register](screenshots/4_register.png) |

| Home | Profile | Alamat Pengiriman | Konfirmasi |
|:---:|:---:|:---:|:---:|
| ![Home](screenshots/5_home.png) | ![Profile](screenshots/6_profile.png) | ![Alamat](screenshots/7_alamat.png) | ![Konfirmasi](screenshots/8_konfirmasi.png) |

---

## 📝 Catatan

- Data tidak disimpan permanen. Akun yang didaftarkan dan isi keranjang akan hilang saat aplikasi ditutup.
- Gambar menu memakai emoji agar aplikasi ringan tanpa aset gambar tambahan.

© 2026 Tegar Raditya Islamifasha · Pemrograman Mobile · UIN Sunan Gunung Djati Bandung

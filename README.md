# Sistem Informasi Pengelolaan Kebun Hidroponik

## Deskripsi Singkat Program

Program ini adalah aplikasi berbasis konsol (CLI) yang dibangun menggunakan bahasa pemrograman Java dengan pola arsitektur **MVC (Model - View - Controller)** ditambah lapisan **DAO (Data Access Object)**. Aplikasi ini berfungsi untuk mengelola data tanaman pada kebun hidroponik secara digital. Data disimpan di **database MySQL** dan diakses menggunakan **JDBC**, sehingga data tidak hilang walaupun program ditutup.

Program ini menerapkan konsep Object-Oriented Programming (OOP) berupa **encapsulation, inheritance, polymorphism, abstraction**, serta **interface**.

Entitas utama pada program ini adalah **Tanaman**, yaitu sebuah *abstract class* yang diturunkan menjadi dua jenis tanaman:

* **Tanaman Sayur** (`TanamanSayur`): memiliki atribut khusus berupa tingkat kerenyahan dan masa simpan.
* **Tanaman Buah** (`TanamanBuah`): memiliki atribut khusus berupa skala Brix dan status berbiji.

Melalui menu utama, pengguna dapat melakukan operasi CRUD (Create, Read, Update, Delete) pada data tanaman. Program juga dilengkapi dengan validasi input pada setter (enkapsulasi), validasi input pada `View`, serta blok `try-catch` pada `main` (termasuk `SQLException`) sehingga aplikasi tidak akan crash apabila pengguna memasukkan data yang tidak valid atau database bermasalah.

**Teknologi yang digunakan:** Java (Maven), MySQL 8, dan MySQL Connector/J 8.3.0.

**Cara menjalankan:**
1. Jalankan MySQL pada `localhost:3306` (misalnya melalui XAMPP/Laragon).
2. Buat database `kebun_hidroponik`, lalu import file `kebun_hidroponik.sql` (berisi tabel `tanaman` beserta data awal).
3. Sesuaikan user dan password database pada `config/Koneksi.java` jika berbeda (default: user `root`, password kosong).
4. Jalankan class `main.Minpro4`.

## Penjelasan Struktur Package

<img width="481" height="407" alt="image" src="https://github.com/user-attachments/assets/abd035ef-7748-4f23-a2c6-5368af1c035b" />

| Package | File | Penjelasan |
|---|---|---|
| `main` | `Minpro4.java` | Class yang berisi method `main`. Menampilkan menu utama dalam perulangan, membaca pilihan pengguna, memanggil method pada controller, dan menangani exception (`NoSuchElementException`, `SQLException`, `IllegalArgumentException`, `Exception`). |
| `config` | `Koneksi.java` | Menyimpan URL, user, dan password database serta menyediakan method `getConnection()` untuk membuat koneksi JDBC. |
| `controller` | `TanamanController.java` | Mengatur alur setiap menu: meminta input ke `View`, melakukan validasi (misalnya cek ID sudah dipakai), lalu memanggil DAO untuk menyimpan/mengambil data. |
| `dao` | `TanamanDAO.java` | *Interface* yang mendefinisikan operasi database untuk tanaman (tambah, ambil semua, cari, update, hapus). |
| `dao` | `TanamanDAOCRUD.java` | Implementasi `TanamanDAO` yang berisi query SQL menggunakan JDBC. |
| `model` | `Tanaman.java` | *Abstract class* (superclass) yang memuat atribut dan method umum semua tanaman. |
| `model` | `TanamanSayur.java` | Subclass dari `Tanaman` untuk jenis sayur, sekaligus mengimplementasikan interface `PerawatanTanaman`. |
| `model` | `TanamanBuah.java` | Subclass dari `Tanaman` untuk jenis buah, sekaligus mengimplementasikan interface `PerawatanTanaman`. |
| `model` | `PerawatanTanaman.java` | *Interface* yang mendefinisikan kontrak perawatan tanaman (`cekNutrisi()` dan `jadwalPenyiraman()`). |
| `view` | `TanamanView.java` | Menangani seluruh input dan output ke konsol (menu, daftar tanaman, pesan, error) beserta validasi input (teks tidak kosong, angka, desimal, yes/no). |

Pada database, kedua jenis tanaman disimpan dalam satu tabel `tanaman`. Kolom `kategori` menandakan jenis tanaman. Kolom `tingkat_kerenyahan` dan `masa_simpan` hanya terisi untuk sayur, sedangkan `skala_brix` dan `berbiji` hanya terisi untuk buah (kolom lainnya bernilai `NULL`).

## Penjelasan Alur Program

1. **Program Dijalankan**

   `Minpro4` membuat objek `TanamanView` dan `TanamanController`. Saat controller dibuat, objek `TanamanDAOCRUD` ikut dibuat untuk menangani seluruh akses ke database. Data awal diambil dari tabel `tanaman` pada database.

2. **Menu Utama**

   ```
   === Sistem Informasi Pengelolaan Kebun Hidroponik ===
   1. Tambah Tanaman
   2. Tampilkan Tanaman
   3. Update Tanaman
   4. Hapus Tanaman
   5. Keluar
   ```

   Menu ditampilkan berulang sampai pengguna memilih menu 5 (Keluar). Jika pilihan di luar 1-5, program menampilkan pesan error dan kembali ke menu.

3. **Menu 1 - Tambah Tanaman**

   Pengguna memasukkan ID (angka, minimal 1). Controller mengecek ke database apakah ID sudah digunakan; jika sudah, proses dibatalkan dengan pesan error. Jika belum, pengguna mengisi nama, grade, dan sistem irigasi, lalu memilih kategori:
   * **1 (Sayur)**: input tingkat kerenyahan dan masa simpan (hari, minimal 1).
   * **2 (Buah)**: input skala Brix (rentang 1 - 16) dan status berbiji (`yes`/`no`).
   * Kategori selain 1 dan 2 akan ditolak dengan pesan error.

   Objek tanaman yang terbentuk dikirim ke DAO untuk disimpan dengan query `INSERT`.

4. **Menu 2 - Tampilkan Tanaman**

   Controller meminta seluruh data ke DAO (`SELECT`), lalu `View` menampilkan setiap tanaman beserta informasi umum, informasi khusus sesuai jenisnya, estimasi masa panen, perawatan khusus, nutrisi, dan jadwal penyiraman. Jika tabel kosong, ditampilkan pesan "Data tanaman kosong."

5. **Menu 3 - Update Tanaman**

   Pengguna memasukkan ID tanaman yang ingin diubah. Jika ID tidak ditemukan di database, muncul pesan error. Jika ditemukan, pengguna mengisi data baru (nama, grade, sistem irigasi) dan data khusus sesuai kategori. Nilai baru dipasang ke objek melalui setter (sehingga tetap tervalidasi), lalu objek dikirim ke DAO untuk disimpan dengan query `UPDATE`. ID dan kategori tanaman tidak dapat diubah.

6. **Menu 4 - Hapus Tanaman**

   Pengguna memasukkan ID tanaman yang ingin dihapus. DAO menjalankan query `DELETE` dan mengembalikan `true` jika ada data yang terhapus, atau `false` jika ID tidak ditemukan (muncul pesan error).

7. **Menu 5 - Keluar**

   Program menampilkan pesan perpisahan, perulangan berhenti, dan `Scanner` ditutup melalui `view.tutup()`.

> **Catatan:** tambahkan screenshot tampilan program (menu utama, tambah, tampil, update, hapus) dan isi tabel `tanaman` pada database di bagian ini.

## Penjelasan Penerapan Encapsulation dan Inheritance

### Encapsulation

Seluruh atribut pada class model dideklarasikan dengan access modifier **`private`** (atau **`protected`** pada atribut yang diwariskan di `Tanaman`), sehingga tidak dapat diakses langsung dari luar class. Akses dilakukan melalui **getter** dan **setter** publik yang melakukan validasi:

* `Tanaman`: `setNamaTanaman()`, `setGradeTanaman()`, `setSistemIrigasi()` menolak `null` atau string kosong/spasi saja, dan menghapus spasi di awal/akhir dengan `trim()`.
* `TanamanSayur`:
  * `setTingkatKerenyahan()` menolak string kosong.
  * `setMasaSimpan()` memvalidasi masa simpan minimal `masaSimpanMin` (1 hari).
* `TanamanBuah`:
  * `setSkalaBrix()` memvalidasi nilai Brix berada pada rentang `brixMin` - `brixMax` (1 - 16).
* Validasi gagal akan melempar `IllegalArgumentException` yang ditangkap di `Minpro4` dan ditampilkan sebagai pesan error. Validasi ini juga berlaku saat objek dibuat dari data database, karena constructor memanggil setter.

Penerapan lain:
* Atribut `idTanaman` dideklarasikan **`private final`**: nilainya hanya dapat diisi sekali melalui constructor dan tidak disediakan setter, sehingga identitas unik tanaman terlindungi.
* Konstanta seperti `brixMin`, `brixMax`, `masaSimpanMin`, `masaPanen`, `masaPanenBerbiji`, dan `masaPanenTanpaBiji` dideklarasikan `static final` (sebagian `private`) agar aturan bisnis tidak dapat diubah dari luar.
* Setter dideklarasikan **`final`** dan dipanggil di dalam constructor, sehingga validasi selalu berlaku sejak objek dibuat dan tidak dapat di-override oleh subclass.
* Detail koneksi database (`URL`, `USER`, `PASS`) dideklarasikan `private static final` pada class `Koneksi`, sehingga tidak dapat diakses langsung dari class lain.

### Inheritance

Program menerapkan relasi pewarisan sebagai berikut:

* **Superclass**: `Tanaman` memuat atribut umum (`idTanaman`, `namaTanaman`, `gradeTanaman`, `sistemIrigasi`), getter/setter, method `tampilkanInfo()`, dan method `kataKata()` yang dideklarasikan `final` sehingga tidak dapat di-override oleh subclass.
* **Subclass 1**: `TanamanSayur extends Tanaman` menambahkan atribut khusus sayur (`tingkatKerenyahan`, `masaSimpan`).
* **Subclass 2**: `TanamanBuah extends Tanaman` menambahkan atribut khusus buah (`skalaBrix`, `berbiji`).

Kedua subclass memanggil constructor superclass melalui `super(...)` untuk mengisi atribut yang diwariskan, kemudian menginisialisasi atribut miliknya sendiri. Pada `TanamanSayur` dan `TanamanBuah` juga tersedia constructor tambahan dengan nilai default (`this(...)`), yaitu kerenyahan "Standar" dengan masa simpan 7 hari untuk sayur, dan Brix 8 tidak berbiji untuk buah.

## Penjelasan Penerapan Polymorphism dan Abstraction

### Abstraction

`Tanaman` dideklarasikan sebagai **abstract class**, sehingga tidak dapat dibuat objeknya secara langsung (hanya bisa lewat `TanamanSayur` atau `TanamanBuah`). Class ini memiliki tiga **abstract method** yang wajib diimplementasikan oleh setiap subclass:

| Abstract method | `TanamanSayur` | `TanamanBuah` |
|---|---|---|
| `getKategori()` | Mengembalikan `"Sayur"` | Mengembalikan `"Buah"` |
| `hitungMasaPanen()` | Selalu 35 hari | 90 hari jika berbiji, 75 hari jika tidak berbiji |
| `tampilkanPerawatanKhusus()` | Menjaga suhu larutan nutrisi di bawah 25 derajat C | Memasang penyangga batang dan membantu penyerbukan |

Abstraction juga diterapkan pada lapisan DAO. Interface `TanamanDAO` hanya mendefinisikan "apa" yang dapat dilakukan terhadap data tanaman (`tambahTanaman`, `ambilSemuaTanaman`, `cariTanaman`, `updateTanaman`, `hapusTanaman`), sedangkan "bagaimana" caranya (query SQL) ditentukan di `TanamanDAOCRUD`. `TanamanController` hanya bergantung pada interface `TanamanDAO`, sehingga jika database diganti cukup dibuat implementasi DAO baru tanpa mengubah controller.

### Polymorphism

1. **Method overriding (runtime polymorphism)**
   * `tampilkanInfo()` di `Tanaman` di-override oleh `TanamanSayur` dan `TanamanBuah`. Masing-masing memanggil `super.tampilkanInfo()` terlebih dahulu, lalu menambahkan atribut spesifiknya.
   * `getKategori()`, `hitungMasaPanen()`, dan `tampilkanPerawatanKhusus()` diimplementasikan dengan perilaku berbeda di setiap subclass.
   * `cekNutrisi()` dan `jadwalPenyiraman()` dari interface `PerawatanTanaman` diimplementasikan berbeda oleh sayur dan buah.

2. **Upcasting pada koleksi**
   * Data hasil query dikumpulkan dalam satu `ArrayList<Tanaman>` yang berisi campuran sayur dan buah. Pada `TanamanView.tampilkanDetail(Tanaman t)`, method `t.tampilkanInfo()`, `t.hitungMasaPanen()`, dan `t.tampilkanPerawatanKhusus()` dipanggil melalui tipe referensi `Tanaman`, namun yang dijalankan otomatis sesuai objek aslinya (`TanamanSayur` atau `TanamanBuah`).
   * Pada `TanamanView.tampilkanDetail()`, objek dicek dengan `instanceof PerawatanTanaman` lalu di-cast ke `PerawatanTanaman` untuk menampilkan nutrisi dan jadwal penyiraman.
   * Pada `updateTanaman()`, `getKategori()` digunakan untuk menentukan *downcasting* ke `TanamanSayur` atau `TanamanBuah` agar atribut khusus dapat diubah.

3. **Method overloading (compile-time polymorphism)**
   * `TanamanController.tambahTanaman()` memiliki 4 versi: tanpa parameter (input interaktif), dengan objek `Tanaman`, dengan parameter sayur, dan dengan parameter buah.
   * `TanamanView.inputAngka(String)` dan `inputAngka(String, int)`.
   * Constructor `TanamanSayur` dan `TanamanBuah` masing-masing memiliki 2 versi.

## Penjelasan Penerapan JDBC

JDBC digunakan untuk menghubungkan program dengan database MySQL `kebun_hidroponik`. Seluruh kodenya berada pada package `config` dan `dao`, dengan dependency `mysql-connector-j` versi 8.3.0 yang didefinisikan di `pom.xml`.

### Koneksi database

Class `Koneksi` (`config/Koneksi.java`) menyediakan method `getConnection()` yang memanggil `DriverManager.getConnection(URL, USER, PASS)` dengan URL `jdbc:mysql://localhost:3306/kebun_hidroponik`.

### Operasi CRUD pada `TanamanDAOCRUD`

| Operasi | Method DAO | Query SQL | Eksekusi |
|---|---|---|---|
| Create | `tambahTanaman()` | `INSERT INTO tanaman ...` | `executeUpdate()` |
| Read | `ambilSemuaTanaman()` | `SELECT * FROM tanaman ORDER BY id_tanaman` | `executeQuery()` dan perulangan `ResultSet` |
| Read | `cariTanaman(id)` | `SELECT * FROM tanaman WHERE id_tanaman = ?` | `executeQuery()` |
| Update | `updateTanaman()` | `UPDATE tanaman SET ... WHERE id_tanaman = ?` | `executeUpdate()` |
| Delete | `hapusTanaman(id)` | `DELETE FROM tanaman WHERE id_tanaman = ?` | `executeUpdate() > 0` |

### Detail penerapan

* **`PreparedStatement`**: seluruh query memakai parameter `?` yang diisi melalui `setInt()`, `setString()`, `setDouble()`, dan `setBoolean()`, sehingga input pengguna tidak digabung langsung ke string SQL dan aman dari SQL injection.
* **`try-with-resources`**: `Connection`, `PreparedStatement`, dan `ResultSet` dibuka di dalam `try (...)` sehingga otomatis ditutup walaupun terjadi error.
* **Penanganan nilai `NULL`**: karena satu tabel dipakai untuk dua jenis tanaman, kolom yang tidak relevan diisi `NULL` melalui `ps.setNull(...)`. Hal ini dilakukan pada method helper `isiDataKhusus()` yang menggunakan `instanceof` untuk membedakan sayur dan buah.
* **Mapping `ResultSet` ke objek**: method helper `buatTanaman(ResultSet rs)` membaca kolom `kategori`, lalu membuat objek `TanamanSayur` atau `TanamanBuah` yang sesuai, sehingga hasil query langsung berupa objek model.
* **Penanganan error**: seluruh method DAO mendeklarasikan `throws SQLException`, diteruskan melalui controller, dan ditangkap di `Minpro4` yang menampilkan pesan "Database bermasalah" tanpa menghentikan program.
* **Pemisahan tanggung jawab**: controller tidak berisi query SQL sama sekali; seluruh akses database dilakukan melalui interface `TanamanDAO`.

package controller;

import dao.TanamanDAO;
import dao.TanamanDAOCRUD;
import java.sql.SQLException;
import model.Tanaman;
import model.TanamanBuah;
import model.TanamanSayur;
import view.TanamanView;

public class TanamanController {
    private TanamanDAO tanamanDAO;
    private TanamanView view;

    public TanamanController(TanamanView view) {
        this.tanamanDAO = new TanamanDAOCRUD();
        this.view = view;
    }

//TAMBAH
    public void tambahTanaman() throws SQLException {
        int id = view.inputAngka("ID Tanaman (Angka): ", 1);

        if (cariTanaman(id) != null) {
            view.tampilkanError("ID Tanaman " + id + " sudah digunakan");
            return;
        }

        String nama = view.inputTeks("Nama Tanaman: ");
        String grade = view.inputTeks("Grade Tanaman: ");
        String sistem = view.inputTeks("Sistem Irigasi: ");
        int kategori = view.inputAngka("Kategori (1. Sayur / 2. Buah): ");

        switch (kategori) {
            case 1 -> {
                String kerenyahan = view.inputTeks("Tingkat Kerenyahan: ");
                int masaSimpan = view.inputAngka("Masa Simpan (hari): ", TanamanSayur.masaSimpanMin);

                tambahTanaman(id, nama, grade, sistem, kerenyahan, masaSimpan);
                view.tampilkanPesan("Tanaman berhasil ditambahkan");
            }
            case 2 -> {
                double brix = view.inputDesimal("Skala Brix (" + TanamanBuah.brixMin + " - " + TanamanBuah.brixMax + "): ",
                        TanamanBuah.brixMin, TanamanBuah.brixMax);
                boolean berbiji = view.inputYesNo("Apakah Tanaman Berbiji? (yes/no): ");

                tambahTanaman(id, nama, grade, sistem, brix, berbiji);
                view.tampilkanPesan("Tanaman berhasil ditambahkan");
            }
            default -> view.tampilkanError("Kategori tidak valid!");
        }
    }

    public void tambahTanaman(Tanaman t) throws SQLException {
        if (t == null) {
            throw new IllegalArgumentException("Data tanaman tidak boleh null");
        }
        if (cariTanaman(t.getIdTanaman()) != null) {
            throw new IllegalArgumentException("ID Tanaman " + t.getIdTanaman() + " sudah digunakan");
        }
        tanamanDAO.tambahTanaman(t);
    }

    public void tambahTanaman(int id, String nama, String grade, String sistem, String kerenyahan, int masaSimpan) throws SQLException {
        tambahTanaman(new TanamanSayur(id, nama, grade, sistem, kerenyahan, masaSimpan));
    }

    public void tambahTanaman(int id, String nama, String grade, String sistem, double brix, boolean berbiji) throws SQLException {
        tambahTanaman(new TanamanBuah(id, nama, grade, sistem, brix, berbiji));
    }

//BACA
    public void tampilkanTanaman() throws SQLException {
        view.tampilkanDaftar(tanamanDAO.ambilSemuaTanaman());
    }

//UPDATE
    public void updateTanaman() throws SQLException {
        int idTarget = view.inputAngka("Masukkan ID Tanaman yang ingin diupdate: ", 1);
        Tanaman t = cariTanaman(idTarget);

        if (t == null) {
            view.tampilkanError("Data tanaman dengan ID " + idTarget + " tidak ada");
            return;
        }

        String namaBaru = view.inputTeks("Nama Tanaman Baru: ");
        String gradeBaru = view.inputTeks("Grade Tanaman Baru: ");
        String sistemBaru = view.inputTeks("Sistem Irigasi Baru: ");

        switch (t.getKategori()) {
            case Tanaman.kategoriSayur -> {
                TanamanSayur ts = (TanamanSayur) t;
                String kerenyahanBaru = view.inputTeks("Tingkat Kerenyahan Baru: ");
                int masaSimpanBaru = view.inputAngka("Masa Simpan Baru (hari): ", TanamanSayur.masaSimpanMin);

                ts.setNamaTanaman(namaBaru);
                ts.setGradeTanaman(gradeBaru);
                ts.setSistemIrigasi(sistemBaru);
                ts.setTingkatKerenyahan(kerenyahanBaru);
                ts.setMasaSimpan(masaSimpanBaru);
            }
            case Tanaman.kategoriBuah -> {
                TanamanBuah tb = (TanamanBuah) t;
                double brixBaru = view.inputDesimal("Skala Brix Baru (" + TanamanBuah.brixMin + " - " + TanamanBuah.brixMax + "): ",
                        TanamanBuah.brixMin, TanamanBuah.brixMax);
                boolean berbijiBaru = view.inputYesNo("Apakah Tanaman Berbiji? (yes/no): ");

                tb.setNamaTanaman(namaBaru);
                tb.setGradeTanaman(gradeBaru);
                tb.setSistemIrigasi(sistemBaru);
                tb.setSkalaBrix(brixBaru);
                tb.setBerbiji(berbijiBaru);
            }
            default -> {
                view.tampilkanError("Kategori tanaman tidak dikenal");
                return;
            }
        }

        tanamanDAO.updateTanaman(t);
        view.tampilkanPesan("Data berhasil diupdate");
    }

//HAPUS
    public void hapusTanaman() throws SQLException {
        int idTarget = view.inputAngka("Masukkan ID Tanaman yang ingin dihapus: ", 1);

        if (tanamanDAO.hapusTanaman(idTarget)) {
            view.tampilkanPesan("Tanaman berhasil dihapus");
        } else {
            view.tampilkanError("Data tanaman dengan ID " + idTarget + " tidak ada");
        }
    }

    public Tanaman cariTanaman(int id) throws SQLException {
        return tanamanDAO.cariTanaman(id);
    }
}
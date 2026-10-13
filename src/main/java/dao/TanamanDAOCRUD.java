package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import model.Tanaman;
import model.TanamanBuah;
import model.TanamanSayur;

public class TanamanDAOCRUD implements TanamanDAO {

//TAMBAH
    @Override
    public void tambahTanaman(Tanaman t) throws SQLException {
        String sql = "INSERT INTO tanaman (id_tanaman, nama_tanaman, grade_tanaman, sistem_irigasi, kategori, "
                   + "tingkat_kerenyahan, masa_simpan, skala_brix, berbiji) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getIdTanaman());
            ps.setString(2, t.getNamaTanaman());
            ps.setString(3, t.getGradeTanaman());
            ps.setString(4, t.getSistemIrigasi());
            ps.setString(5, t.getKategori());
            isiDataKhusus(ps, t, 6);
            ps.executeUpdate();
        }
    }

//LIHAT
    @Override
    public ArrayList<Tanaman> ambilSemuaTanaman() throws SQLException {
        ArrayList<Tanaman> daftar = new ArrayList<>();
        String sql = "SELECT * FROM tanaman ORDER BY id_tanaman";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(buatTanaman(rs));
            }
        }
        return daftar;
    }

    @Override
    public Tanaman cariTanaman(int id) throws SQLException {
        String sql = "SELECT * FROM tanaman WHERE id_tanaman = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return buatTanaman(rs);
                }
            }
        }
        return null;
    }

//UPDATE
    @Override
    public void updateTanaman(Tanaman t) throws SQLException {
        String sql = "UPDATE tanaman SET nama_tanaman = ?, grade_tanaman = ?, sistem_irigasi = ?, "
                   + "tingkat_kerenyahan = ?, masa_simpan = ?, skala_brix = ?, berbiji = ? WHERE id_tanaman = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNamaTanaman());
            ps.setString(2, t.getGradeTanaman());
            ps.setString(3, t.getSistemIrigasi());
            isiDataKhusus(ps, t, 4);
            ps.setInt(8, t.getIdTanaman());
            ps.executeUpdate();
        }
    }

//HAPUS
    @Override
    public boolean hapusTanaman(int id) throws SQLException {
        String sql = "DELETE FROM tanaman WHERE id_tanaman = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

//HELPER
    private void isiDataKhusus(PreparedStatement ps, Tanaman t, int awal) throws SQLException {
        if (t instanceof TanamanSayur) {
            TanamanSayur ts = (TanamanSayur) t;
            ps.setString(awal, ts.getTingkatKerenyahan());
            ps.setInt(awal + 1, ts.getMasaSimpan());
            ps.setNull(awal + 2, Types.DECIMAL);
            ps.setNull(awal + 3, Types.BOOLEAN);
        } else if (t instanceof TanamanBuah) {
            TanamanBuah tb = (TanamanBuah) t;
            ps.setNull(awal, Types.VARCHAR);
            ps.setNull(awal + 1, Types.INTEGER);
            ps.setDouble(awal + 2, tb.getSkalaBrix());
            ps.setBoolean(awal + 3, tb.isBerbiji());
        }
    }

    private Tanaman buatTanaman(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_tanaman");
        String nama = rs.getString("nama_tanaman");
        String grade = rs.getString("grade_tanaman");
        String sistem = rs.getString("sistem_irigasi");
        String kategori = rs.getString("kategori");

        if (kategori.equals(Tanaman.kategoriSayur)) {
            return new TanamanSayur(id, nama, grade, sistem, rs.getString("tingkat_kerenyahan"), rs.getInt("masa_simpan"));
        }
        return new TanamanBuah(id, nama, grade, sistem, rs.getDouble("skala_brix"), rs.getBoolean("berbiji"));
    }
}
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Tanaman;

public interface TanamanDAO {
    void tambahTanaman(Tanaman t) throws SQLException;

    ArrayList<Tanaman> ambilSemuaTanaman() throws SQLException;

    Tanaman cariTanaman(int id) throws SQLException;

    void updateTanaman(Tanaman t) throws SQLException;

    boolean hapusTanaman(int id) throws SQLException;
}
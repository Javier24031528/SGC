package org.example.sgc.dao;

import org.example.sgc.model.Materia;
import org.example.sgc.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    public void insert(Materia materia) throws SQLException {
        String sql = "INSERT INTO materias (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, materia.getNombre());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    materia.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Materia> findAll() throws SQLException {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT * FROM materias";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                materias.add(new Materia(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return materias;
    }

    public void update(Materia materia) throws SQLException {
        String sql = "UPDATE materias SET nombre = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, materia.getNombre());
            pstmt.setInt(2, materia.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM materias WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}

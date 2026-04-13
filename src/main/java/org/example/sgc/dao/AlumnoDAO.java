package org.example.sgc.dao;

import org.example.sgc.model.Alumno;
import org.example.sgc.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO implements GenericDAO<Alumno> {

    @Override
    public void insert(Alumno alumno) throws SQLException {
        String sql = "INSERT INTO alumnos (nombre, email) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, alumno.getNombre());
            pstmt.setString(2, alumno.getEmail());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alumno.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Alumno> findAll() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alumnos.add(new Alumno(rs.getInt("id"), rs.getString("nombre"), rs.getString("email")));
            }
        }
        return alumnos;
    }

    @Override
    public void update(Alumno alumno) throws SQLException {
        String sql = "UPDATE alumnos SET nombre = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, alumno.getNombre());
            pstmt.setString(2, alumno.getEmail());
            pstmt.setInt(3, alumno.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM alumnos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}

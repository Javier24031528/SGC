package org.example.sgc.dao;

import org.example.sgc.model.Calificacion;
import org.example.sgc.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalificacionDAO {

    public void insert(Calificacion calif) throws SQLException {
        String sql = "INSERT INTO calificaciones (alumno_id, materia_id, nota, periodo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, calif.getAlumnoId());
            pstmt.setInt(2, calif.getMateriaId());
            pstmt.setDouble(3, calif.getNota());
            pstmt.setString(4, calif.getPeriodo());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calif.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Calificacion> findAllWithDetails() throws SQLException {
        List<Calificacion> list = new ArrayList<>();
        String sql = "SELECT c.*, a.nombre as nombre_alumno, m.nombre as nombre_materia " +
                     "FROM calificaciones c " +
                     "JOIN alumnos a ON c.alumno_id = a.id " +
                     "JOIN materias m ON c.materia_id = m.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Calificacion c = new Calificacion(
                    rs.getInt("id"),
                    rs.getInt("alumno_id"),
                    rs.getInt("materia_id"),
                    rs.getDouble("nota"),
                    rs.getString("periodo")
                );
                c.setNombreAlumno(rs.getString("nombre_alumno"));
                c.setNombreMateria(rs.getString("nombre_materia"));
                list.add(c);
            }
        }
        return list;
    }

    public void update(Calificacion calif) throws SQLException {
        String sql = "UPDATE calificaciones SET nota = ?, periodo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, calif.getNota());
            pstmt.setString(2, calif.getPeriodo());
            pstmt.setInt(3, calif.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM calificaciones WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public List<Calificacion> findByAlumno(int alumnoId) throws SQLException {
        List<Calificacion> list = new ArrayList<>();
        String sql = "SELECT c.*, a.nombre as nombre_alumno, m.nombre as nombre_materia " +
                     "FROM calificaciones c " +
                     "JOIN alumnos a ON c.alumno_id = a.id " +
                     "JOIN materias m ON c.materia_id = m.id " +
                     "WHERE c.alumno_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, alumnoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Calificacion c = new Calificacion(
                        rs.getInt("id"),
                        rs.getInt("alumno_id"),
                        rs.getInt("materia_id"),
                        rs.getDouble("nota"),
                        rs.getString("periodo")
                    );
                    c.setNombreAlumno(rs.getString("nombre_alumno"));
                    c.setNombreMateria(rs.getString("nombre_materia"));
                    list.add(c);
                }
            }
        }
        return list;
    }
}

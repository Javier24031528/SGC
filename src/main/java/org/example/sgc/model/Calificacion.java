package org.example.sgc.model;

public class Calificacion {
    private int id;
    private int alumnoId;
    private int materiaId;
    private double nota;
    private String periodo;

    // Campos auxiliares para la UI
    private String nombreAlumno;
    private String nombreMateria;

    public Calificacion() {}

    public Calificacion(int id, int alumnoId, int materiaId, double nota, String periodo) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.materiaId = materiaId;
        this.nota = nota;
        this.periodo = periodo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }
    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getNombreAlumno() { return nombreAlumno; }
    public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }
    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public String getEstado() {
        return nota >= 6.0 ? "Aprobado" : "Reprobado";
    }
}

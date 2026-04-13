package org.example.sgc.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.sgc.dao.AlumnoDAO;
import org.example.sgc.dao.CalificacionDAO;
import org.example.sgc.dao.MateriaDAO;
import org.example.sgc.model.Alumno;
import org.example.sgc.model.Calificacion;
import org.example.sgc.model.Materia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML private TextField txtNombreAlumno;
    @FXML private TextField txtEmailAlumno;
    @FXML private TableView<Alumno> tblAlumnos;
    @FXML private TableColumn<Alumno, Integer> colIdAlumno;
    @FXML private TableColumn<Alumno, String> colNombreAlumno;
    @FXML private TableColumn<Alumno, String> colEmailAlumno;

    @FXML private TextField txtNombreMateria;
    @FXML private TableView<Materia> tblMaterias;
    @FXML private TableColumn<Materia, Integer> colIdMateria;
    @FXML private TableColumn<Materia, String> colNombreMateria;

    @FXML private ComboBox<Alumno> cmbAlumno;
    @FXML private ComboBox<Materia> cmbMateria;
    @FXML private TextField txtNota;
    @FXML private TextField txtPeriodo;
    @FXML private TableView<Calificacion> tblCalificaciones;
    @FXML private TableColumn<Calificacion, String> colCalAlumno;
    @FXML private TableColumn<Calificacion, String> colCalMateria;
    @FXML private TableColumn<Calificacion, Double> colCalNota;
    @FXML private TableColumn<Calificacion, String> colCalPeriodo;
    @FXML private TableColumn<Calificacion, String> colCalEstado;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private MateriaDAO materiaDAO = new MateriaDAO();
    private CalificacionDAO calificacionDAO = new CalificacionDAO();

    private ObservableList<Alumno> alumnosList = FXCollections.observableArrayList();
    private ObservableList<Materia> materiasList = FXCollections.observableArrayList();
    private ObservableList<Calificacion> calificacionesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas Alumnos
        colIdAlumno.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreAlumno.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmailAlumno.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblAlumnos.setItems(alumnosList);

        // Configurar columnas Materias
        colIdMateria.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreMateria.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tblMaterias.setItems(materiasList);

        // Configurar columnas Calificaciones
        colCalAlumno.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        colCalMateria.setCellValueFactory(new PropertyValueFactory<>("nombreMateria"));
        colCalNota.setCellValueFactory(new PropertyValueFactory<>("nota"));
        colCalPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colCalEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblCalificaciones.setItems(calificacionesList);

        // Usar Platform.runLater para no bloquear la inicialización de la UI
        try {
            javafx.application.Platform.runLater(this::loadData);
        } catch (NoClassDefFoundError | Exception e) {
            System.err.println("Error crítico de JavaFX: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            alumnosList.setAll(alumnoDAO.findAll());
            materiasList.setAll(materiaDAO.findAll());
            calificacionesList.setAll(calificacionDAO.findAllWithDetails());
            
            cmbAlumno.setItems(alumnosList);
            cmbMateria.setItems(materiasList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error al cargar datos: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAlumno() {
        String nombre = txtNombreAlumno.getText();
        String email = txtEmailAlumno.getText();
        if (nombre.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Datos inválidos. Verifique el email.");
            return;
        }
        try {
            Alumno a = new Alumno(0, nombre, email);
            alumnoDAO.insert(a);
            alumnosList.add(a);
            txtNombreAlumno.clear();
            txtEmailAlumno.clear();
        } catch (SQLException e) {
            showAlert("Error al guardar alumno: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddMateria() {
        String nombre = txtNombreMateria.getText();
        if (nombre.isEmpty()) return;
        try {
            Materia m = new Materia(0, nombre);
            materiaDAO.insert(m);
            materiasList.add(m);
            txtNombreMateria.clear();
        } catch (SQLException e) {
            showAlert("Error al guardar materia: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCalificacion() {
        Alumno a = cmbAlumno.getValue();
        Materia m = cmbMateria.getValue();
        String notaStr = txtNota.getText();
        String periodo = txtPeriodo.getText();

        if (a == null || m == null || notaStr.isEmpty() || periodo.isEmpty()) {
            showAlert("Complete todos los campos.");
            return;
        }

        try {
            double nota = Double.parseDouble(notaStr);
            Calificacion c = new Calificacion(0, a.getId(), m.getId(), nota, periodo);
            calificacionDAO.insert(c);
            loadData(); // Recargar para obtener nombres
            txtNota.clear();
            txtPeriodo.clear();
        } catch (NumberFormatException e) {
            showAlert("Nota inválida.");
        } catch (SQLException e) {
            showAlert("Error al guardar calificación: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(tblCalificaciones.getScene().getWindow());

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                document.add(new Paragraph("Reporte de Calificaciones - SGC"));
                document.add(new Paragraph(" "));

                PdfPTable table = new PdfPTable(5);
                table.addCell("Alumno");
                table.addCell("Materia");
                table.addCell("Nota");
                table.addCell("Periodo");
                table.addCell("Estado");

                for (Calificacion c : calificacionesList) {
                    table.addCell(c.getNombreAlumno());
                    table.addCell(c.getNombreMateria());
                    table.addCell(String.valueOf(c.getNota()));
                    table.addCell(c.getPeriodo());
                    table.addCell(c.getEstado());
                }

                document.add(table);
                document.close();
                showAlert("Reporte PDF generado con éxito.");
            } catch (Exception e) {
                showAlert("Error al generar PDF: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(tblCalificaciones.getScene().getWindow());

        if (file != null) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                String[] header = {"Alumno", "Materia", "Nota", "Periodo", "Estado"};
                writer.writeNext(header);

                for (Calificacion c : calificacionesList) {
                    String[] data = {
                            c.getNombreAlumno(),
                            c.getNombreMateria(),
                            String.valueOf(c.getNota()),
                            c.getPeriodo(),
                            c.getEstado()
                    };
                    writer.writeNext(data);
                }
                showAlert("Reporte CSV generado con éxito.");
            } catch (Exception e) {
                showAlert("Error al generar CSV: " + e.getMessage());
            }
        }
    }
    @FXML private void handleExit() { System.exit(0); }

    private void showAlert(String msg) {
        System.out.println("[DEBUG_LOG] " + msg);
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(msg);
            alert.show();
        });
    }
}

package org.example.sgc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.sgc.dao.UsuarioDAO;
import org.example.sgc.model.Usuario;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor, complete todos los campos.");
            return;
        }

        try {
            Usuario user = usuarioDAO.login(username, password);
            if (user != null) {
                System.out.println("[DEBUG_LOG] Login exitoso para usuario: " + username);
                loadMainApp();
            } else {
                System.out.println("[DEBUG_LOG] Fallo de login: Credenciales incorrectas para: " + username);
                showAlert("Error", "Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            System.err.println("[DEBUG_LOG] Error de base de datos durante login: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error de Base de Datos", "No se pudo conectar a la base de datos. Verifique que MySQL esté activo y las credenciales en DatabaseConnection.java sean correctas.");
        } catch (IOException e) {
            System.err.println("[DEBUG_LOG] Error de E/S al cargar la vista: " + e.getMessage());
            showAlert("Error de Aplicación", "No se pudo cargar la vista principal.");
        }
    }

    private void loadMainApp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sgc/view/main.fxml"));
        Parent root = loader.load();
        
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("SGC - Panel Principal");
        stage.centerOnScreen();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

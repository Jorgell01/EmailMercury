package pgv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pgv.dao.UsuarioDAO;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    private Stage dialogStage;
    private boolean loginSuccessful = false;
    private String email;
    private String password;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @FXML
    private void handleLogin() {
        email = emailField.getText();
        password = passwordField.getText();

        try {
            if (UsuarioDAO.validarCredenciales(email, password)) {
                loginSuccessful = true;
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Inicio de sesión exitoso.");
                dialogStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Credenciales incorrectas.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al iniciar sesión.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            int userId = UsuarioDAO.registrarUsuario(name, email, password);
            if (userId != -1) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado con éxito.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al registrar el usuario.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al registrar el usuario.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    @FXML
    private void handleChangePassword() {
        String email = emailField.getText();
        String newPassword = passwordField.getText();

        try {
            int userId = UsuarioDAO.obtenerUsuarioId(email);
            if (userId != -1) {
                if (UsuarioDAO.cambiarContrasenia(userId, newPassword)) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Contraseña cambiada con éxito.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error al cambiar la contraseña.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Usuario no encontrado.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cambiar la contraseña.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowPasswordChangeDate() {
        String email = emailField.getText();

        try {
            int userId = UsuarioDAO.obtenerUsuarioId(email);
            if (userId != -1) {
                String fechaCambioContrasenia = UsuarioDAO.obtenerFechaCambioContrasenia(userId);
                if (fechaCambioContrasenia != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Fecha de Cambio de Contraseña", "Último cambio de contraseña: " + fechaCambioContrasenia);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo obtener la fecha de cambio de contraseña.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Usuario no encontrado.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al obtener la fecha de cambio de contraseña.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
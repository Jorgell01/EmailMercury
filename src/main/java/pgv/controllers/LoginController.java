package pgv.controllers;

import javafx.fxml.FXML;
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
    private TextField nameField; // Add this field if it is used in the FXML

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
                dialogStage.close();
            } else {
                System.out.println("Credenciales incorrectas.");
            }
        } catch (Exception e) {
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
                System.out.println("Usuario registrado con éxito.");
            } else {
                System.out.println("Error al registrar el usuario.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
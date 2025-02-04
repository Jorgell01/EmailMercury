package pgv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pgv.email.EmailReceiver;
import pgv.email.EmailSender;
import pgv.model.Email;

import javax.mail.AuthenticationFailedException;
import java.util.List;

public class EmailController {

    @FXML
    private TextField toField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea bodyField;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TableView<Email> emailsTable;
    @FXML
    private TableColumn<Email, String> remitenteColumn;
    @FXML
    private TableColumn<Email, String> destinatarioColumn;
    @FXML
    private TableColumn<Email, String> asuntoColumn;
    @FXML
    private TableColumn<Email, String> cuerpoColumn;

    private String remitente;
    private String password;

    public void setCredentials(String remitente, String password) {
        this.remitente = remitente;
        this.password = password;
    }

    @FXML
    private void initialize() {
        remitenteColumn.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        destinatarioColumn.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        asuntoColumn.setCellValueFactory(new PropertyValueFactory<>("asunto"));
        cuerpoColumn.setCellValueFactory(new PropertyValueFactory<>("cuerpo"));
    }

    @FXML
    private void handleSend() {
        String destinatario = toField.getText();
        String asunto = subjectField.getText();
        String cuerpo = bodyField.getText();

        if (remitente == null || remitente.isEmpty() ||
                password == null || password.isEmpty() ||
                destinatario == null || destinatario.isEmpty() ||
                asunto == null || asunto.isEmpty() ||
                cuerpo == null || cuerpo.isEmpty()) {
            System.out.println("Error: Todos los campos deben estar completos.");
            return;
        }

        try {
            EmailSender.enviarCorreo(remitente, password, destinatario, asunto, cuerpo);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadEmails() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            System.out.println("All fields must be filled.");
            return;
        }

        try {
            List<Email> emails = EmailReceiver.recibirCorreos(username, password);
            emailsTable.getItems().setAll(emails);
        } catch (Exception e) {
            System.out.println("Error receiving emails.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearSendFields() {
        toField.clear();
        subjectField.clear();
        bodyField.clear();
    }

    @FXML
    private void handleClearReceiveFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        emailsTable.getItems().clear();
    }
}
package pgv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import pgv.email.EmailReceiver;
import pgv.model.Email;

import javax.mail.AuthenticationFailedException;
import java.util.List;

public class ReceiveEmailController {

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

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        remitenteColumn.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        destinatarioColumn.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        asuntoColumn.setCellValueFactory(new PropertyValueFactory<>("asunto"));
        cuerpoColumn.setCellValueFactory(new PropertyValueFactory<>("cuerpo"));
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
        } catch (AuthenticationFailedException e) {
            System.out.println("Error: Invalid username or password.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error receiving emails.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
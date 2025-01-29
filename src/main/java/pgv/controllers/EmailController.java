package pgv.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pgv.email.EmailReceiver;
import pgv.email.EmailSender;
import pgv.model.Email;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmailController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        remitenteColumn.setCellValueFactory(new PropertyValueFactory<>("remitente"));
        destinatarioColumn.setCellValueFactory(new PropertyValueFactory<>("destinatario"));
        asuntoColumn.setCellValueFactory(new PropertyValueFactory<>("asunto"));
        cuerpoColumn.setCellValueFactory(new PropertyValueFactory<>("cuerpo"));
    }

    @FXML
    private void handleEnviarCorreo() {
        try {
            EmailSender.enviarCorreo(
                    1, // ID del usuario
                    "user1@localhost", // Remitente
                    "1234", // Contraseña
                    "user2@localhost", // Destinatario
                    "Muy buenas chavales", // Asunto
                    "Prueba de correo para ver que se envía en Thunderbird" // Cuerpo
            );
            System.out.println("Correo enviado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRecibirCorreos() {
        try {
            List<Email> emails = EmailReceiver.recibirCorreos(
                    2, // ID del usuario
                    "user2", // Intentar con user2 o user2@localhost
                    "abcd" // Contraseña
            );
            System.out.println("Correos recibidos con éxito.");
            emailsTable.getItems().setAll(emails);
        } catch (Exception e) {
            System.out.println("Error al recibir correos.");
            e.printStackTrace();
        }
    }


}
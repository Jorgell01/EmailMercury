package pgv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pgv.email.EmailSender;

public class SendEmailController {

    @FXML
    private TextField toField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea bodyField;

    private Stage dialogStage;
    private String remitente;
    private String password;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCredentials(String remitente, String password) {
        this.remitente = remitente;
        this.password = password;
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
            dialogStage.close();
        } catch (Exception e) {
            System.out.println("Error al enviar el correo.");
            e.printStackTrace();
        }
    }
}
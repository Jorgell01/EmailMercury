package pgv.email;

import pgv.config.DatabaseConfig;

import javax.mail.Message;
import javax.mail.PasswordAuthentication; // Cambia al paquete correcto
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class EmailSender {
    public static void enviarCorreo(int userId, String remitente, String password, String destinatario, String asunto, String cuerpo) throws Exception {
        // Configuración de envío con JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");

        // Crear sesión autenticada
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        // Crear el mensaje
        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        // Enviar el mensaje
        Transport.send(mensaje);

        // Guardar el correo en la base de datos
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "INSERT INTO emails (user_id, remitente, destinatario, asunto, cuerpo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, remitente);
            stmt.setString(3, destinatario);
            stmt.setString(4, asunto);
            stmt.setString(5, cuerpo);
            stmt.executeUpdate();
        }
    }
}


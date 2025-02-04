package pgv.email;

import pgv.config.DatabaseConfig;
import pgv.dao.UsuarioDAO;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class EmailSender {
    public static void enviarCorreo(String remitente, String password, String destinatario, String asunto, String cuerpo) throws Exception {
        if (remitente == null || remitente.isEmpty() || password == null || password.isEmpty() ||
                destinatario == null || destinatario.isEmpty() || asunto == null || asunto.isEmpty() ||
                cuerpo == null || cuerpo.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos deben estar completos.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true"); // Activar logs SMTP

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto);
        mensaje.setContent(cuerpo, "text/plain; charset=UTF-8"); // Set encoding to UTF-8

        System.out.println("Enviando correo a " + destinatario);
        Transport.send(mensaje);
        System.out.println("Correo enviado con éxito.");

        // Save the sent email to the database
        guardarCorreoEnviado(remitente, destinatario, asunto, cuerpo);
    }

    private static void guardarCorreoEnviado(String remitente, String destinatario, String asunto, String cuerpo) throws Exception {
        int userId = UsuarioDAO.obtenerUsuarioId(remitente);
        if (userId == -1) {
            throw new IllegalArgumentException("Usuario no encontrado: " + remitente);
        }

        String sql = "INSERT INTO emails (user_id, remitente, destinatario, asunto, cuerpo, fecha_envio) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConfig.getConnection()) {
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
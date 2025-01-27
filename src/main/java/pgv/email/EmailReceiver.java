package pgv.email;

import pgv.model.Email;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailReceiver {
    public static List<Email> recibirCorreos(int userId, String usuario, String password) throws Exception {
        List<Email> emails = new ArrayList<>();

        // Configuración del servidor POP3
        Properties props = new Properties();
        props.put("mail.pop3.host", "localhost");
        props.put("mail.pop3.port", "110");
        props.put("mail.pop3.auth", "true");

        // Crear sesión para POP3
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, password);
            }
        });

        // Conectar al servidor POP3
        Store store = session.getStore("pop3");
        store.connect("localhost", usuario, password);

        // Abrir la carpeta INBOX
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        // Listar los mensajes
        for (Message mensaje : folder.getMessages()) {
            String remitente = mensaje.getFrom()[0].toString();
            String asunto = mensaje.getSubject();
            String cuerpo = mensaje.getContent().toString();
            String destinatario = usuario; // El destinatario es el usuario autenticado

            // Agregar a la lista de emails
            emails.add(new Email(remitente, destinatario, asunto, cuerpo));
        }

        // Cerrar la conexión
        folder.close(false);
        store.close();

        return emails;
    }
}

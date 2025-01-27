package pgv.email;

import javax.mail.*;
import java.util.Properties;

public class EmailReceiver {
    public static void recibirCorreos(int userId, String usuario, String password) throws Exception {
        // Configuración del servidor POP3
        Properties props = new Properties();
        props.put("mail.pop3.host", "localhost"); // Servidor POP3
        props.put("mail.pop3.port", "110");       // Puerto POP3 (por defecto es 110)
        props.put("mail.pop3.auth", "true");      // Activar autenticación

        // Crear sesión para POP3
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, password);
            }
        });

        // Conectar al servidor POP3
        Store store = session.getStore("pop3");
        store.connect("localhost", usuario, password); // Autenticación

        // Abrir la carpeta INBOX
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        // Listar los mensajes
        for (Message mensaje : folder.getMessages()) {
            System.out.println("Asunto: " + mensaje.getSubject());
            System.out.println("De: " + mensaje.getFrom()[0]);
            System.out.println("Cuerpo: " + mensaje.getContent());
        }

        // Cerrar la conexión
        folder.close(false);
        store.close();
    }
}

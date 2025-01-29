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
        props.put("mail.debug", "true"); // Activa logs de depuración

        System.out.println("Intentando conectar con el servidor POP3...");

        // Crear sesión para POP3
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Autenticando como: " + usuario);
                return new PasswordAuthentication(usuario, password);
            }
        });

        // Conectar al servidor POP3
        try (Store store = session.getStore("pop3")) {
            store.connect("localhost", usuario, password);
            System.out.println("Conexión POP3 exitosa.");

            // Abrir la bandeja de entrada
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            System.out.println("Correos encontrados: " + folder.getMessageCount());

            for (Message mensaje : folder.getMessages()) {
                String remitente = mensaje.getFrom()[0].toString();
                String asunto = mensaje.getSubject();
                String cuerpo = mensaje.getContent().toString();
                String destinatario = usuario;

                emails.add(new Email(remitente, destinatario, asunto, cuerpo));
            }

            folder.close(false);
        } catch (AuthenticationFailedException e) {
            System.out.println("Error: Usuario o contraseña incorrectos.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Otro error al recibir correos.");
            e.printStackTrace();
        }

        return emails;
    }
}


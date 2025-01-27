package pgv;

import pgv.email.EmailSender;
import pgv.email.EmailReceiver;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Enviar un correo
            EmailSender.enviarCorreo(
                    1, // ID del usuario
                    "user1@localhost", // Remitente
                    "1234", // Contraseña
                    "user2@localhost", // Destinatario
                    "Muy buenas chavales", // Asunto
                    "Prueba de correo para ver que se envía en Thunderbird" // Cuerpo
            );
            System.out.println("Correo enviado con éxito.");

            // Recibir correos
            EmailReceiver.recibirCorreos(
                    2, // ID del usuario
                    "user2@localhost", // Usuario
                    "abcd" // Contraseña
            );
            System.out.println("Correos recibidos con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
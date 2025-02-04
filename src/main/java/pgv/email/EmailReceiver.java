package pgv.email;

import pgv.model.Email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailReceiver {

    private static final String MAIL_PATH = "C:\\xampp\\MercuryMail\\MAIL";

    public static List<Email> recibirCorreos(String usuario, String password) throws IOException {
        List<Email> emails = new ArrayList<>();
        String userDirPath = MAIL_PATH + "\\" + usuario;

        File userDir = new File(userDirPath);
        if (!userDir.exists() || !userDir.isDirectory()) {
            throw new IOException("User directory does not exist: " + userDirPath);
        }

        File[] emailFiles = userDir.listFiles((dir, name) -> name.endsWith(".CNM"));
        if (emailFiles != null) {
            for (File emailFile : emailFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(emailFile))) {
                    String remitente = reader.readLine();
                    String destinatario = reader.readLine();
                    String asunto = reader.readLine();
                    StringBuilder cuerpo = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        cuerpo.append(line).append("\n");
                    }
                    emails.add(new Email(remitente, destinatario, asunto, cuerpo.toString()));
                }
            }
        }
        return emails;
    }
}
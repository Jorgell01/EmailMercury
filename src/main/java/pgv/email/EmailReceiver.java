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
        String userDirPath = MAIL_PATH + "\\" + usuario.split("@")[0]; // Extract username from email

        File userDir = new File(userDirPath);
        if (!userDir.exists() || !userDir.isDirectory()) {
            throw new IOException("User directory does not exist: " + userDirPath);
        }

        File[] emailFiles = userDir.listFiles((dir, name) -> name.endsWith(".CNM"));
        if (emailFiles != null) {
            for (File emailFile : emailFiles) {
                Email email = readEmailFromFile(emailFile);
                if (email != null) {
                    emails.add(email);
                }
            }
        }
        return emails;
    }

    private static Email readEmailFromFile(File emailFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(emailFile))) {
            String from = null;
            String to = null;
            String subject = null;
            StringBuilder body = new StringBuilder();
            String line;
            boolean isBody = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("From: ")) {
                    from = line.substring(6);
                } else if (line.startsWith("To: ")) {
                    to = line.substring(4);
                } else if (line.startsWith("Subject: ")) {
                    subject = line.substring(9);
                } else if (line.isEmpty()) {
                    isBody = true;
                } else if (isBody) {
                    body.append(line).append("\n");
                }
            }

            if (from != null && to != null && subject != null) {
                return new Email(from, to, subject, body.toString().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
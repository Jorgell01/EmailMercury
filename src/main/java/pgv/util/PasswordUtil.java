package pgv.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Method to hash a password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Method to check a password against a hashed password
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid salt version: " + e.getMessage());
            return false;
        }
    }
}
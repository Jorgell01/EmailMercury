package pgv.util;

public class PasswordHasher {
    public static void main(String[] args) {
        String plainTextPassword = "1234";
        String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
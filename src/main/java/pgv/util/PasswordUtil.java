package pgv.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Generar un hash de una contraseña
    public static String hashPassword(String password) {
        // Gensalt genera un "salt" único para cada contraseña
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verificar una contraseña contra su hash
    public static boolean checkPassword(String password, String hashedPassword) {
        // Compara la contraseña sin hashear con el hash almacenado
        return BCrypt.checkpw(password, hashedPassword);
    }
}

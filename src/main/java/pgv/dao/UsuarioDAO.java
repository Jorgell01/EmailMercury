package pgv.dao;

import pgv.config.DatabaseConfig;
import pgv.util.CommandUtil;
import pgv.util.PasswordUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    private static final String MAIL_PATH = "C:\\xampp\\MercuryMail\\MAIL";
    private static final String USER_FILE_PATH = "C:\\xampp\\MercuryMail\\MAIL\\PMAIL.USR";

    // Registrar un nuevo usuario
    public static int registrarUsuario(String nombre, String correo, String contrasenia) throws Exception {
        String sql = "INSERT INTO users (nombre, correo, password_hash, fecha_cambio_contrasenia) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            String hashedPassword = PasswordUtil.hashPassword(contrasenia);

            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);

                // Write to the user file
                writeUserToFile(nombre, nombre);

                // Create user directory and write password
                createUserDirectoryAndWritePassword(nombre, contrasenia);

                // Restart Mercury service
                CommandUtil.restartMercuryService();

                return userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error registering user: " + e.getMessage());
        }
        return -1;
    }

    private static void writeUserToFile(String username, String personalName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            writer.write("U;" + username + ";(None)");
            writer.newLine();
        }
    }

    private static void createUserDirectoryAndWritePassword(String nombre, String contrasenia) throws IOException {
        String userDirPath = MAIL_PATH + "\\" + nombre;
        java.io.File userDir = new java.io.File(userDirPath);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        // Write password to PASSWD.PM
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userDirPath + "\\PASSWD.PM"))) {
            writer.write("# Mercury/32 User Information File\n");
            writer.write("POP3_access: " + contrasenia + "\n");
            writer.write("APOP_secret: \n");
        }
    }

    // Actualizar la contraseña de un usuario
    public static boolean cambiarContrasenia(int userId, String nuevaContrasenia) throws Exception {
        String sql = "UPDATE users SET password_hash = ?, fecha_cambio_contrasenia = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            String hashedPassword = PasswordUtil.hashPassword(nuevaContrasenia);

            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        }
    }

    // Obtener el ID de un usuario a partir del correo
    public static int obtenerUsuarioId(String correo) throws Exception {
        String sql = "SELECT id FROM users WHERE correo = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    // Validar las credenciales del usuario
    public static boolean validarCredenciales(String email, String password) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT password_hash FROM users WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                return PasswordUtil.checkPassword(password, hashedPassword);
            } else {
                return false;
            }
        }
    }

    // Obtener la fecha del último cambio de contraseña
    public static String obtenerFechaCambioContrasenia(int userId) throws Exception {
        String sql = "SELECT fecha_cambio_contrasenia FROM users WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("fecha_cambio_contrasenia");
            }
        }
        return null; // Usuario no encontrado
    }
}
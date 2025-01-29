package pgv.dao;

import pgv.config.DatabaseConfig;
import pgv.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    // Registrar un nuevo usuario
    public static int registrarUsuario(String nombre, String correo, String contrasenia) throws Exception {
        String sql = "INSERT INTO users (nombre, correo, contrasenia, fecha_cambio_contrasenia) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // Hashear la contraseña
            String hashedPassword = PasswordUtil.hashPassword(contrasenia);

            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();

            // Obtener el ID del usuario recién creado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Devuelve el ID del nuevo usuario
            }
        }
        return -1; // Error al registrar el usuario
    }

    // Actualizar la contraseña de un usuario
    public static boolean cambiarContrasenia(int userId, String nuevaContrasenia) throws Exception {
        String sql = "UPDATE users SET contrasenia = ?, fecha_cambio_contrasenia = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Hashear la nueva contraseña
            String hashedPassword = PasswordUtil.hashPassword(nuevaContrasenia);

            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0; // Devuelve true si la contraseña fue actualizada
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
        return -1; // Usuario no encontrado
    }

    // Validar las credenciales del usuario
    public static boolean validarCredenciales(String correo, String contrasenia) throws Exception {
        String sql = "SELECT contrasenia FROM users WHERE correo = ?";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("contrasenia");
                return PasswordUtil.checkPassword(contrasenia, hashedPassword);
            }
        }
        return false; // Credenciales inválidas
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
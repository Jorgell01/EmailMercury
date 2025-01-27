package pgv.dao;

import pgv.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    public static int registrarUsuario(String nombre, String correo, String contrasenia) throws Exception {
        String sql = "INSERT INTO users (nombre, correo, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, contrasenia);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Devuelve el ID del nuevo usuario
            }
        }
        return -1;
    }

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
}

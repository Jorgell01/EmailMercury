package pgv.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mariadb://localhost:3306/emailmercury"; // Cambia 'localhost' y 'emailmercury' si es necesario.
    private static final String USER = "root"; // Tu usuario de MariaDB.
    private static final String PASSWORD = ""; // Tu contraseña de MariaDB.

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

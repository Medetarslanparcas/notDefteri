package swing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/kisiselnotdefteri";
    private static final String USER = "root";
    private static final String PASSWORD = "?????6789Az";

    public static Connection connect() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Veritabanına bağlantı sağlanamadı!", e);
        }
    }
}

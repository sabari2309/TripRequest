import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static DB instance;
    private Connection connection;
    private String url = "jdbc:postgresql://localhost:5432/Tripdatabase";
    private String username = "postgres";
    private String password = "Sabari@2309";

    private DB() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DB getInstance() throws SQLException {
        if (instance == null) {
            instance = new DB();
        } else if (instance.getConnection().isClosed()) {
            instance = new DB();
        }

        return instance;
    }
}

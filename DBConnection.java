import java.sql.*;

public class DBConnection {

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");

            String connUrl = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(connUrl, "postgres", "admin");

            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
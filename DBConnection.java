import java.sql.*;

public class DBConnection {

    public static void main(String args[]){
        //System.out.println(new DBConnection().connect());
    }

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");

            String connUrl = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(connUrl, "postgres", "4321");

        //    System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
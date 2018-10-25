import java.sql.*;
import java.util.Scanner;

// java -cp .;postgresql-42.2.5.jar mainApp
public class mainApp {

    private static Scanner sc;

    public static void main(String args[]) {

        sc = new Scanner(System.in);

        mainApp m = new mainApp();
        m.connectDB();

        sc = new Scanner(System.in);
        System.out.println(
                "----------------------------------------------------------------------------------------------");
        System.out.println("                         Welcome To Vehicle Maintanence System");
        System.out.println(
                "----------------------------------------------------------------------------------------------");

        while (true) {
            System.out.println("\nList Of tables:-");
            System.out.println("\n1. Customers");
            System.out.println("\n2. Vehicles");
            System.out.println("\n3. Service Providers");
            System.out.println("\n4. Services");
            System.out.println("\n5. Staff Details");
            System.out.println("\n6. Feedbacks");
            System.out.println("\n7. Manufacturers");
            System.out.println("\n8. Payments");
            System.out.println("\n9. Top Queries");
            System.out.println("\n10. Exit");
            System.out.println("\n Enter your choice[1-10]:");

            int choice = sc.nextInt();

            switch(choice){

                case 1:
                    m.customers();
                    break;

                case 10:
                    break;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    public void connectDB() {

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String connUrl = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(connUrl, "postgres", "admin");

            if (conn != null) {
                System.out.println("Opened database successfully");
            } else {
                System.out.println("Couldn't opened database");
            }

        } catch (Exception e) {

        }
    }

    public void customers() {
        Customers c = new Customers();
        System.out.println(
                "----------------------------------------------------------------------------------------------");
        System.out.println("\n 1. View");
        System.out.println("\n 2. Insert");
        System.out.println("\n 3. Update");
        System.out.println("\n 4. Delete");
        System.out.println("\n 5. Exit");
        System.out.println(
                "----------------------------------------------------------------------------------------------");
        int cch = sc.nextInt();

        switch (cch) {
        case 1:
            c.viewCustomers();
            break;
        case 2:
            c.insertCustomer();
            break;
        case 3:
            c.updateCustomer();
            break;
        case 4:
            c.deleteCustomer();
            break;
        case 5:
            break;
        default:
            System.out.println("Wrong Choice");
        }
    }

}
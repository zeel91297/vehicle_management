import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Customers {

    private static Scanner sc = null;

    public static void main(String[] args) {

        Customers c = new Customers();
        sc = new Scanner(System.in);
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

    public void viewCustomers() {
        String cname, cemail, ccontact, caddress;
        Integer cid;
        Connection conn = new DBConnection().connect();
        Statement stmt = null;
        if (conn != null) {

            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from vehicle_mgmt.customers");

                System.out.println("ID  " + " " + "Name       " + "Email-id     " + "Contact No   " + "Address");

                while (rs.next()) {

                    cname = rs.getString("cname");
                    cemail = rs.getString("cemail");
                    ccontact = rs.getString("ccontact");
                    caddress = rs.getString("caddress");

                    cid = rs.getInt("cid");

                    System.out.println(cid + "    " + cname + "    " + cemail + "    " + ccontact + "     " + caddress);
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertCustomer() {
        Connection conn = new DBConnection().connect();
        PreparedStatement stmt = null;

        System.out.println("Enter Customer Name:");
        String cname = sc.next();
        System.out.println("Enter Customer Email-ID:");
        String cemail = sc.next();
        System.out.println("Enter Customer Contact No:");
        Long ccontact = sc.nextLong();
        System.out.println("Enter Customer Address:");
        String caddress = sc.next();
        String query = "insert into vehicle_mgmt.customers (cname,cemail,ccontact,caddress) values(?,?,?,?)";
        if (conn != null) {
            try {
                stmt = conn.prepareStatement(query);

                stmt.setString(1, cname);
                stmt.setString(2, cemail);
                stmt.setLong(3, ccontact);
                stmt.setString(4, caddress);
                stmt.executeUpdate();

                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCustomer() {
        Connection conn = new DBConnection().connect();
        PreparedStatement stmt = null;

        System.out.println("Enter Customer ID:");
        int cid = sc.nextInt();

        System.out.println("Enter Customer Name:");
        String cname = sc.next();

        String query = "update vehicle_mgmt.customers set cname=? where cid=?";
        try {

            stmt = conn.prepareStatement(query);
            stmt.setString(1, cname);
            stmt.setInt(2, cid);

            stmt.executeUpdate();
            stmt.close();
            System.out.println("Updated");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void deleteCustomer() {

        Connection conn=new DBConnection().connect();
        PreparedStatement stmt=null;

        System.out.println("Enter Customer ID:");
        int cid = sc.nextInt();

        String query = "delete from vehicle_mgmt.customers where cid=?";
        try {
            
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, cid);

            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Deleted");
            }
            else{
                System.out.println("Couldn't delete");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
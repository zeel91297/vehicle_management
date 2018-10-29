import java.sql.*;
import java.util.*;

public class ComplexQueries {

    private static Scanner sc = null;

    public static void main(String[] args) {

        sc = new Scanner(System.in);
        ComplexQueries c = new ComplexQueries();
        System.out.println(
                "----------------------------------------------------------------------------------------------");
        System.out.println("\n 1. List all staff name who had serviced all vehicles");
        System.out.println("\n 2. List all the customers who had given feedback for all services.");
        System.out.println(
                "\n 3. Retrieve which vehicle modal requires least maintainance than other vehicle modals for particular vehicle type.");
        System.out.println(
                "\n 4. Retrieve top 5 staff details who had maximum ratings in their service feedback for particular vehicle type.");
        System.out.println("\n 5. Retrieve vehicle types who required maximum service");
        System.out.println("\n 6. Exit");
        System.out.println(
                "----------------------------------------------------------------------------------------------");
        int cch = sc.nextInt();

        switch (cch) {
        case 1:
            c.query1();
            break;
        case 2:
            c.query2();
            break;
        case 3:
            c.query3();
            break;
        case 4:
            c.query4();
            break;
        case 5:
            c.query5();
            break;
        case 6:
            break;
        default:
            System.out.println("Wrong Choice");
        }
    }

    public void query1() {
        Connection conn = new DBConnection().connect();
        Statement stmt = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String query = "select st1.staff_name from vehicle_mgmt.staff_details as st1 join(select s2.staff_id as staff_id,v2.model_id from vehicle_mgmt.services as s2 join vehicle_mgmt.vehicles as v2 on s2.vehicle_id=v2.vehicle_id except (select s1.staff_id,v1.model_id from vehicle_mgmt.services as s1 cross join vehicle_mgmt.vehicle_models as v1) except (select s2.staff_id,v2.model_id from vehicle_mgmt.services as s2 join vehicle_mgmt.vehicles as v2 on s2.vehicle_id=v2.vehicle_id)) as t1 on t1.staff_id=st1.staff_id;";
                ResultSet rs = stmt.executeQuery(query);

                System.out.println("Staff Name");
                System.out.println("-----------");
                while (rs.next()) {
                    String staff_name = rs.getString("staff_name");

                    System.out.println(staff_name);
                }

                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void query2() {
        Connection conn = new DBConnection().connect();
        Statement stmt = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String query = "select v1.cid,v3.cname from vehicle_mgmt.vehicles as v1 join (select t1.vid as vehcle_id from (select count(j2.feed_id) as cnt1,j1.vehicle_id as vid from vehicle_mgmt.services as j1 join vehicle_mgmt.feedbacks as j2 on j2.service_id=j1.service_id group by j1.vehicle_id) as t1 join (select count(cnt2.service_id) as cnt2,cnt2.vehicle_id as vid from vehicle_mgmt.services as cnt2 group by cnt2.vehicle_id) as t2 on t1.vid=t2.vid where t1.cnt1=t2.cnt2) as v2 on v1.vehicle_id=v2.vehcle_id join vehicle_mgmt.customers as v3 on v1.cid=v3.cid;";
                ResultSet rs = stmt.executeQuery(query);

                System.out.println("Customer ID " + "Customer Name");
                System.out.println("-------------------------------");
                while (rs.next()) {
                    int cid = rs.getInt("cid");
                    String cname = rs.getString("cname");

                    System.out.println("   " + cid + "      | " + cname);
                }

                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void query3() {
        Scanner scc = new Scanner(System.in);
        Connection conn = new DBConnection().connect();
        PreparedStatement stmt = null;
        System.out.println("Enter Vehicle type name:");
        String ttype = scc.next();
        if (conn != null) {
            try {
                String query = 
                "select vm.model_name,t2.average_cost as maintance_cost from vehicle_mgmt.vehicle_models as vm join (select (t1.smc/t1.nvr)::integer as average_cost,t1.mid as model_id,t1.nvr as no_of_v from (select sum(eg.no_of_vehicles_registered) as nvr,sum(eg.no_of_time_it_requires_service) as nrs ,sum(eg.sum_of_maintance_cost) as smc,eg.model_id as mid from (select count(df.vid) as no_of_vehicles_registered,df.c2 as no_of_time_it_requires_service,df.c3 as sum_of_maintance_cost,df.mid as model_id from (select vv.vehicle_id as vid,(select count(*) from vehicle_mgmt.services as ss where ss.vehicle_id=vv.vehicle_id)as c2, (select sum(pp.amount) from vehicle_mgmt.payments as pp join vehicle_mgmt.services as ss on ss.pay_id=pp.pay_id where ss.vehicle_id=vv.vehicle_id) as c3, vm.model_id as mid from vehicle_mgmt.vehicles as vv join vehicle_mgmt.vehicle_models as vm on vv.model_id=vm.model_id join vehicle_mgmt.vehicle_types as vt on vm.type_id=vt.type_id where vt.type_name=?) as df group by mid,df.c2,df.c3) as eg group by eg.model_id) as t1)as t2 on t2.model_id=vm.model_id order by  case when t2.average_cost is null then 0 else 0 end,maintance_cost desc nulls first LIMIT 5";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, ttype);
                ResultSet rs = stmt.executeQuery();

                System.out.println("Model Name"+"||"+"Maintanence Cost");
                System.out.println("-----------");
                while (rs.next()) {
                    String model_name = rs.getString("model_name");
                    int average_cost=rs.getInt("maintance_cost");
                    System.out.println(model_name + "||" + average_cost);
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void query4() {
        Scanner scc = new Scanner(System.in);
        Connection conn = new DBConnection().connect();
        PreparedStatement stmt = null;
        System.out.println("Enter Vehicle type name:");
        String ttype = scc.next();
        if (conn != null) {
            try {
                String query = "select sd.staff_name from vehicle_mgmt.staff_details as sd where sd.staff_id in (select distinct(hj.gh) as sids from (select ss.staff_id as gh,ff.feed_ratings from vehicle_mgmt.services as ss join vehicle_mgmt.feedbacks as ff on ss.service_id=ff.service_id where ss.vehicle_id IN (select vv.vehicle_id from vehicle_mgmt.vehicles as vv where vv.model_id IN (select vm.model_id from vehicle_mgmt.vehicle_models as vm join vehicle_mgmt.vehicle_types as vt on vm.type_id=vt.type_id where vt.type_name=?)) order by ff.feed_ratings desc LIMIT 5) as hj);";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, ttype);
                ResultSet rs = stmt.executeQuery();

                System.out.println("Staff Name");
                System.out.println("-----------");
                while (rs.next()) {
                    String staff_name = rs.getString("staff_name");
                    System.out.println(staff_name);
                }
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void query5() {
        Connection conn = new DBConnection().connect();
        Statement stmt = null;
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                String query = "select vtt.type_name from vehicle_mgmt.vehicle_types as vtt where vtt.type_id IN(select cg.typess as sty from (select count(vt.type_id) as vhcl_t,vt.type_id as typess from vehicle_mgmt.vehicle_types as vt join vehicle_mgmt.vehicle_models as vm on vm.type_id=vt.type_id join vehicle_mgmt.vehicles as v on vm.model_id=v.model_id join vehicle_mgmt.services as s on v.vehicle_id=s.vehicle_id group by vt.type_id order by vhcl_t desc limit 1) as cg);";
                ResultSet rs = stmt.executeQuery(query);

                System.out.println("Vehicle Type Name");
                System.out.println("------------------");
                while (rs.next()) {
                    String type_name = rs.getString("type_name");

                    System.out.println(type_name);
                }

                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
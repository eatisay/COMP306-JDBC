import java.sql.*;
import com.mysql.jdbc.Connection; 
public class SQLManup {
      static Connection con = null;
      public static void main(String[] args) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(
                       "jdbc:mysql://istavrit.eng.ku.edu.tr:3306/eatisay15_db",
                       "eatisay15", "HoWQ2usl6QQCT6qh");
            System.out.println("Connection successful");
        } catch (Exception e) {
            System.out.println(e);
        }
        Statement alter;
		try {
			alter = con.createStatement();
			alter.execute("ALTER TABLE CUSTOMER " + 
			          "ADD COLUMN  current_budget DECIMAL DEFAULT 0, " + 
			          "ADD COLUMN  max_budget DECIMAL, " + 
			          "ADD COLUMN  min_budget DECIMAL");
			System.out.println("Altered succesfully");
			alter.execute("ALTER TABLE EMPLOYEE " + 
			          "ADD COLUMN  salary_bonus DECIMAL");
			System.out.println("Altered table Employee succesfully");
			alter.execute("UPDATE CUSTOMER	" + 
					"  SET current_budget=0");
			System.out.println("Updated table Customer succesfully");
			alter.execute("UPDATE CUSTOMER as C" + 
					"  SET current_budget=" + 
					"  case when (SELECT sum(EVENT_REQ.estimated_cost)" + 
					"    FROM  EVENT_REQ" + 
					"    WHERE C.customer_id=EVENT_REQ.customer_id AND EVENT_REQ.status='Approved') IS NOT NULL THEN" + 
					"  (SELECT sum(EVENT_REQ.estimated_cost)" + 
					"    FROM  EVENT_REQ" + 
					"    WHERE C.customer_id=EVENT_REQ.customer_id AND EVENT_REQ.status='Approved')" + 
					"  ELSE 0" + 
					"  END" + 
					"  ");
			System.out.println("Updated current_budget succesfully");
			alter.execute("UPDATE CUSTOMER	" + 
					"  SET max_budget=20000.0,"
					+ "min_budget=0");	
			System.out.println("Updated max_budget and min_budget succesfully");
			alter.execute("UPDATE EMPLOYEE " + 
					"  SET salary_bonus=" + 
					"  case when (SELECT sum(ER.estimated_cost) as EC" + 
					"    FROM EVENT_PLAN as EP, EVENT_REQ as ER" + 
					"    WHERE EP.employee_id=EMPLOYEE.employee_id AND EP.event_id=ER.event_id AND status='Approved') is not null then (SELECT sum(ER.estimated_cost) as EC" + 
					"    FROM EVENT_PLAN as EP, EVENT_REQ as ER " + 
					"    WHERE EP.employee_id=EMPLOYEE.employee_id AND EP.event_id=ER.event_id AND status='Approved')" + 
					"  else 0" + 
					"  end");
			System.out.println("Updated salary_bonus succesfully");

			String input = "select name, current_budget from CUSTOMER order by current_budget desc";
			ResultSet output = alter.executeQuery(input);
			while (output.next())
		      {
		        String s = output.getString("name");
		        float n = output.getFloat("current_budget");
		        System.out.println(s + "   " + n);
		      }
			
			System.out.println("Altered succesfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
     }
}
package DBaccess;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

public class Test {

	public static void main(String[] args) {
		
		try{
		 // Create a variable for the connection string.
	      String connectionUrl = "jdbc:sqlserver://f9v27ej0no.database.windows.net;" +
	      "databaseName=Sensor Readings;user=GTAdmin@f9v27ej0no;password=COMP2013g10"; 
	      
	      // Declare the JDBC objects.
	      Connection con = null;
	      Statement stmt = null;
	      ResultSet rs = null;

	      try {
	         // Establish the connection.
	         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         con = DriverManager.getConnection(connectionUrl);

	         // Create and execute an SQL statement that returns some data.
	         String SQL = "SELECT * FROM dbo.sensorRaw";
	         stmt = con.createStatement();
	         rs = stmt.executeQuery(SQL);

	         // Iterate through the data in the result set and display it.
	         while (rs.next()) {
	            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
	         }
	      }

	      // Handle any errors that may have occurred.
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	      finally {
	         if (rs != null) try { rs.close(); } catch(Exception e) {}
	         if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	         if (con != null) try { con.close(); } catch(Exception e) {}
	      }
		}

			catch(Exception a){
					a.printStackTrace();
			}
	}
}
	


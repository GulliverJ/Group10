import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnect {

	private static String connectionString = 
			"jdbc:sqlserver://f9v27ej0no.database.windows.net:1433" + ";" +
					"database=Sensor Readings" + ";" +
					"user=GTAdmin@f9v27ej0no" + ";" +
					"password=COMP2013g10" + ";" +
					
					"encrypt=true" + ";" + 
					"hostNameInCertificate=*.database.windows.net" + ";" +
					"loginTimeout=30" + ";";


	private static Connection connection = null;  // For making the connection
	private static Statement statement = null;    // For the SQL statement
	
	private static void makeConnection() {
		try
		{
		    // Ensure the SQL Server driver class is available.
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		    // Establish the connection.
		    connection = DriverManager.getConnection(connectionString);
			System.out.println("Connection made");
		} catch(Exception e) {
			System.out.println("Exception caught");
		}
	}
	
	public void sendStatement(String query) {
		
		makeConnection();
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Exception caught");
		}
	}
	
}

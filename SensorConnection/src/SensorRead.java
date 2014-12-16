
import java.sql.*;

import com.microsoft.sqlserver.jdbc.*;

public class SensorRead {

	static String connectionString = 
			"jdbc:sqlserver://f9v27ej0no.database.windows.net:1433" + ";" +
					"database=Sensor Readings" + ";" +
					"user=GTAdmin@f9v27ej0no" + ";" +
					"password=COMP2013g10" + ";" +
					
					"encrypt=true" + ";" + 
					"hostNameInCertificate=*.database.windows.net" + ";" +
					"loginTimeout=30" + ";";

	// The types for the following variables are
	// defined in the java.sql library.
	static Connection connection = null;  // For making the connection
	static Statement statement = null;    // For the SQL statement
	
	//This would be code the node runs.
	//Should have a method which takes a sensor ID and updates it.
	//There should also be a method for updating meta data like status depending on the methods.
	//This should loop every 5 minutes whilst it can.
	
	//public
	
	public static void makeConnection() {
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
	
	public static void updateData(int sensorID, String portId) {

		SerialRead getReading = new SerialRead(portId);
		getReading.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
		
		int updatedResult = -1;
		

		
		do {
			updatedResult = getReading.readResult;
			System.out.println("Waiting for result... " + updatedResult);
		} while(updatedResult == -3);
		
	    String sqlString = 
		        "UPDATE sensorRaw " + "SET readingFloat = " + updatedResult + "WHERE sensorID = " + sensorID;
	    System.out.println("Statement: " + sqlString);
		sendStatement(sqlString);
	}
	
	public static void sendStatement(String query) {
		
		makeConnection();
		
		try {
			statement = connection.createStatement();

			// Execute the statement.
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Exception caught");
		}
	}


	public static void main(String[] args) {
	
		updateData(1, "COM7");
		System.out.println("Done");
	
	}
}
	/*		
	
	public static void main(String[] args) {

		
		try
		{
		    // Ensure the SQL Server driver class is available.
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		    // Establish the connection.
		    connection = DriverManager.getConnection(connectionString);

		    // Define the SQL string.
		    String sqlString = 
		        "UPDATE holders " + "SET contactName = 'Gulliver Johnson' " + "WHERE contactName='Victoria Koh'";

		    // Use the connection to create the SQL statement.
		    statement = connection.createStatement();

		    // Execute the statement.
		    statement.executeUpdate(sqlString);

		    // Provide a message when processing is complete.
		    System.out.println("Processing complete.");

		}// Exception handling and resource closing not shown...
		catch(Exception e)
		{
			System.out.println("Exception caught");
		}
	}

}
*/
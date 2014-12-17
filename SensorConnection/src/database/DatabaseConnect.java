/* basically the APIIII~ main bit */

package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnect {

	//Connection string - location of database
	private static String connectionString = 
			"jdbc:sqlserver://f9v27ej0no.database.windows.net:1433" + ";" +
					"database=Sensor Readings" + ";" +
					"user=GTAdmin@f9v27ej0no" + ";" +
					"password=COMP2013g10" + ";" +
					
					"encrypt=true" + ";" + 
					"hostNameInCertificate=*.database.windows.net" + ";" +
					"loginTimeout=30" + ";";

	//Initialises drivers to database
	static {
		// Ensure the SQL Server driver class is available.
	    try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Initialises a connection to the database on the cloud
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connectionString);
	}
	
	//Opens a connection to the database
	//Enables the execution of sql instructions to the database tables
	private static void executeStatement(String query) {
		Connection connection = null;
		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			try {
				statement.execute(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	//in this case, sensorRaw2 is governed by the TimeStamp (getLastupdate) and sensorID as primary keys 
	//a new row/ entry is inserted into the sensorRaw2 table every time a new 'latest update' is sent to the original sensorRaw table
	public static void insertSensorRaw(SensorRaw sensorRaw) {
		executeStatement("insert into dbo.sensorraw2 values("
				+ sensorRaw.getSensorID() + ", "
				+ sensorRaw.getReadingFloat() + ", "
				+ "'" + sensorRaw.getUnit() + "', "
				+ "'" + sensorRaw.getLastUpdate() + "')");
	}
	
	//basically retrieves all data from sensorRaw2 i.e. the resultSet
	//stores the resultSet by manner of arraylist 
	//i.e. each object 'SensorRaw' in the arraylist represents one row in the original table 'resultSet' of data 
	public static List<SensorRaw> getSensorRawList() {
		List<SensorRaw> sensorRawList = new ArrayList<SensorRaw>();
		Connection connection = null;
		try {
			connection = getConnection();
			try {
				PreparedStatement preparedStatement = connection.prepareStatement("select * from dbo.sensorraw2 order by lastupdate asc");
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					SensorRaw sensorRaw = new SensorRaw(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getString(3), resultSet.getTimestamp(4));
					sensorRawList.add(sensorRaw);
				}
				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return sensorRawList;
	}
	
	//Method overloading - accepts timestamp / date from.
	//ignores anything that happens before a specific time stamp - i.e. the most recent update that was read
	public static List<SensorRaw> getSensorRawList(Timestamp from) {
		List<SensorRaw> sensorRawList = new ArrayList<SensorRaw>();
		Connection connection = null;
		try {
			connection = getConnection();
			try {
				PreparedStatement preparedStatement = connection.prepareStatement("select * from dbo.sensorraw2 where lastUpdate > '" + from + "' order by lastupdate asc");
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					SensorRaw sensorRaw = new SensorRaw(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getString(3), resultSet.getTimestamp(4));
					sensorRawList.add(sensorRaw);
				}
				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return sensorRawList;
	}

	//Opens connection to the parking table~
	public static List<Parking> getParkingList() {
		List<Parking> parkingList = new ArrayList<Parking>();
		Connection connection = null;
		try {
			connection = getConnection();
			try {
				PreparedStatement preparedStatement = connection.prepareStatement("select * from dbo.parking");
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Parking parking = new Parking(resultSet.getInt(1), resultSet.getBoolean(2), resultSet.getLong(3), resultSet.getInt(4), resultSet.getBoolean(5));
					parkingList.add(parking);
				}
				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return parkingList;
	}
	
	//Opens a connection to the SensorMeta table 
	//Stores the current data in the table in an arraylist
	//Each object in the array list represents one row in the table. 
	//Aka. you can say each row represents a different sensor
	public static List<SensorMeta> getSensorMetaList() {
		List<SensorMeta> sensorMetaList = new ArrayList<SensorMeta>();
		Connection connection = null;
		try {
			connection = getConnection();
			try {
				PreparedStatement preparedStatement = connection.prepareStatement("select * from dbo.sensormeta");
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					SensorMeta sensorMeta = new SensorMeta(resultSet.getInt(1), resultSet.getInt(2), resultSet.getBoolean(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getDate(8), resultSet.getString(9), resultSet.getFloat(10), resultSet.getFloat(11));
					sensorMetaList.add(sensorMeta);
				}
				resultSet.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return sensorMetaList;
	}
	
	//Enables the updating of the parking table on the cloud. 
	//See 'Parking Analytics' for more details on how this is being worked.
	//Essentially, whether a space is occupied or not and the duration of stay is being calculated there
	//The function below merely provides a way to PUSH the data back into the database 
	public static void updateParking(int sensorID, boolean occupied, long duration) {
		//checking if sensor exists and remove it
		executeStatement("delete from dbo.parking where sensorid = " + sensorID);
		// add new sensor data
		executeStatement("insert into dbo.parking values ("
				+ sensorID + ", "
				+ (occupied ? "1" : "0") + ", "
				+ duration + ", "
				+ 0 + ", " //set by default 'fake/mock value'
				+ 1 + ")"); //again, set by default 'fake/ mock value'
	}
	
	public void sendStatement(String query) {
		executeStatement(query);
	}
	
	/* For testing purposes 
	public static void main(String[] args) {
	//iterates through the sensorRaw2 table and prints it all out (cfm?)
		for (SensorRaw sensorRaw : getSensorRawList()) {
			System.out.println(sensorRaw);
		}
		System.out.println();
		for (SensorMeta sensorMeta : getSensorMetaList()) {
			System.out.println(sensorMeta);
		}
		System.out.println();
		for (Parking parking : getParkingList()) {
			System.out.println(parking);
		}
	}
	*/

}
	
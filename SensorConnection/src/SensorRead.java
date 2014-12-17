
import java.sql.*;

import database.DatabaseConnect;
import database.SensorRaw;


public class SensorRead {
	
	private int sensorID;
	private String portID;
	private DatabaseConnect dbLink = new DatabaseConnect();
	
	public SensorRead(int sensorID, String portID) {
		this.sensorID = sensorID;
		this.portID = portID;		
	}
	
	/* Makes a call to the sensor at portId to gain its current readings, then updates the database */
	public void updateRawData() {

		/* Initialises the communication with the arduino at portId*/
		SerialRead getReading = new SerialRead(this.portID);
		getReading.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		
		/* Gets and sends the new sensor reading */
		int updatedResult = -1;	// Initialised to -1 to avoid erroneous reports of readings
		
		do {
			updatedResult = getReading.readResult;
		} while(updatedResult == -3); // Loops until a reliable reading is returned by the arduino		
		
	    String sqlString = 
		        "UPDATE sensorRaw " +
		        "SET readingFloat = " +
		        updatedResult +
		        " WHERE sensorID = " +
		        sensorID; 
	    dbLink.sendStatement(sqlString);
	    
	    /* Gets and sends the date */
	    java.util.Date date = new java.util.Date();
	    Timestamp time = new Timestamp(date.getTime());
	    sqlString = 
	    		"UPDATE sensorRaw " +
	    		"SET lastUpdate = '" +
	    		time + 
	    		"' WHERE sensorID = " +
	    		sensorID;
	    
		dbLink.sendStatement(sqlString);
		
		/* Updates the sensor's status*/
		updateStatus(updatedResult);
		
		// Update!
		DatabaseConnect.insertSensorRaw(new SensorRaw(sensorID, updatedResult, "cm", time));
	}
	
	private void updateStatus(int reading) {
		boolean status;
		if( reading >= 0)
			status = true;
		else
			status = false;
		
		String sqlString =
		        "UPDATE sensorMeta " +
		        "SET status = '" +
		        status +
		        "' WHERE sensorID = " +
		        this.sensorID;
		dbLink.sendStatement(sqlString);
	}
	
}
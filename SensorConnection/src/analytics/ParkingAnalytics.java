package analytics;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DatabaseConnect;
import database.SensorRaw;

public class ParkingAnalytics {

	private Map<Integer, SensorRaw> sensors;
	private Timestamp mostRecent;
	
	/*
	 * 1. 200.0 cm 2014-12-17 11:25:26.197 -> STORED (first data)
	 * 1. 150.0 cm 2014-12-17 11:25:27.197 -> STORED (replaces the previous data)
	 * 1. 70.0 cm 2014-12-17 11:25:28.197 -> STORED (replaced the previous data)
	 * 1. 70.0 cm 2014-12-17 11:25:29.197 -> IGNORED 
	 * 1. 70.5 cm 2014-12-17 11:25:30.197 -> IGNORED
	 * 1. 70.0 cm 2014-12-17 11:25:31.197 -> IGNORED
	 * 
	 * For the purposes of calculating the state for the sensor with ID 1, the last piece of data
	 * stored is based on the difference between the previous(readingFloat) and curr.(readingFloat) 
	 * (if it is > 1) 
	 * 
	 * By making use of the readingFloat and timeStamp, we are calculating if a spot is occupied or not
	 * To calculate if a spot is occupied, we check if the reading float is between 0 and 200 and its value
	 * has not changed for at least 30 seconds. 
	 * 
	 * Not changing for at least 30 seconds = assumption: most likely stopped & parked
	 * 
	 * And then based off this, if the value has not changed for at least 30 seconds, we count the duration
	 * this lasts for in order to calculate the occupiedStateDuration.
	 * 
	 */
	
	public ParkingAnalytics() {
		// initially we have no data about any sensor
		sensors = new HashMap<Integer, SensorRaw>();
		// we retrieve from the database, all past data about sensors and create an updated view of the current
		// state of all sensors and each parking slot
		// the map will hold each sensorraw value that is of interest to us
		updateParking(DatabaseConnect.getSensorRawList());
		// sleep and try to update the base over and over
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			System.out.println("Starting to update...");
			// update the parking, with the sensor raw data since the last time we retrieved until now
			updateParking(DatabaseConnect.getSensorRawList(mostRecent));
			System.out.println("Updated...");
		}
	}
	
	private void updateParking(List<SensorRaw> sensorRawList) {
		// go over each sensor raw data
		for (SensorRaw sensorRaw : sensorRawList) {
			// keep track of the most recent time that we read, so we know where to start next time
			if (mostRecent == null || sensorRaw.getLastUpdate().after(mostRecent)) {
				mostRecent = sensorRaw.getLastUpdate();
			}
			// if we do not have any previous data for the sensor, then just retrieve the current data
			if (sensors.containsKey(sensorRaw.getSensorID()) == false) {
				sensors.put(sensorRaw.getSensorID(), sensorRaw);
			} else {
				// this means we had previous data about the sensor,
				// we check if the reading is different and if so, then we store the current data
				SensorRaw oldSensorRaw = sensors.get(sensorRaw.getSensorID());
				if (Math.abs(oldSensorRaw.getReadingFloat() - sensorRaw.getReadingFloat()) < 1) {
					// ignore
				} else {
					// replace the old sensor raw with the new data
					sensors.put(sensorRaw.getSensorID(), sensorRaw);
				}
			}
		}
		for (SensorRaw sensorRaw : sensors.values()) {
			// check if the value is between 0 and 200
			boolean occupied = (sensorRaw.getReadingFloat() > 0 && sensorRaw.getReadingFloat() < 200);
			long duration = 0;
			if (occupied) {
				java.util.Date date = new java.util.Date();
			    Timestamp time = new Timestamp(date.getTime());
			    duration = time.getTime() - sensorRaw.getLastUpdate().getTime();
			    // make sure that the value is stable for at least 30 seconds, 
			    //to determine occupation otherwise ignore
			    if (duration < 30000) {
			    	occupied = false;
			    	duration = 0;
			    }
			}
			DatabaseConnect.updateParking(sensorRaw.getSensorID(), occupied, duration);
		}
	}
	
	public static void main(String[] args) {
		new ParkingAnalytics();
	}
	
}

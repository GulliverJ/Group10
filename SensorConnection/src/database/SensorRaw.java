package database;

import java.sql.Timestamp;

public class SensorRaw {
	private int sensorID;
	private float readingFloat;
	private String unit;
	private Timestamp lastUpdate;
	
	public SensorRaw(int sensorID, float readingFloat, String unit, Timestamp lastUpdate) {
		this.sensorID = sensorID;
		this.readingFloat = readingFloat;
		this.unit = unit;
		this.lastUpdate = lastUpdate;
	}
	
	public int getSensorID() {
		return sensorID;
	}
	public float getReadingFloat() {
		return readingFloat;
	}
	public String getUnit() {
		return unit;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	
	public String toString() {
		return sensorID + " " + readingFloat + " " + unit + " " + lastUpdate;
	}
}

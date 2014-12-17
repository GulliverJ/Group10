package database;

import java.util.Date;

public class SensorMeta {
	private int sensorID;
	private int nodeID;
	private boolean status;
	private String measures;
	private String type;
	private String model;
	private int holderID;
	private Date added;
	private String road;
	private float lat;
	private float lng;
		
	public SensorMeta(int sensorID, int nodeID, boolean status,
			String measures, String type, String model, int holderID,
			Date added, String road, float lat, float lng) {
		this.sensorID = sensorID;
		this.nodeID = nodeID;
		this.status = status;
		this.measures = measures;
		this.type = type;
		this.model = model;
		this.holderID = holderID;
		this.added = added;
		this.road = road;
		this.lat = lat;
		this.lng = lng;
	}
	public int getSensorID() {
		return sensorID;
	}
	public int getNodeID() {
		return nodeID;
	}
	public boolean isStatus() {
		return status;
	}
	public String getMeasures() {
		return measures;
	}
	public String getType() {
		return type;
	}
	public String getModel() {
		return model;
	}
	public int getHolderID() {
		return holderID;
	}
	public Date getAdded() {
		return added;
	}
	public String getRoad() {
		return road;
	}
	public float getLat() {
		return lat;
	}
	public float getLng() {
		return lng;
	}
	
	public String toString() {
		return nodeID + " " + status + " " + measures + " " + type + " " + model + " " + holderID + " " + added + " " + road + " " + " " + lat + " " + lng;	
	}
}

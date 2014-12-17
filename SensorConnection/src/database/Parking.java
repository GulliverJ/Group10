package database;

public class Parking {

	private int sensorID;
	private boolean occupied;
	private long occupiedStateDuration;
	private int nearestAvailableID;
	private boolean legallyParked;
	
	public Parking(int sensorID, boolean occupied, long occupiedStateDuration,
			int nearestAvailableID, boolean legallyParked) {
		this.sensorID = sensorID;
		this.occupied = occupied;
		this.occupiedStateDuration = occupiedStateDuration;
		this.nearestAvailableID = nearestAvailableID;
		this.legallyParked = legallyParked;
	}
	
	public int getSensorID() {
		return sensorID;
	}
	public boolean isOccupied() {
		return occupied;
	}
	public long getOccupiedStateDuration() {
		return occupiedStateDuration;
	}
	public int getNearestAvailableID() {
		return nearestAvailableID;
	}
	public boolean isLegallyParked() {
		return legallyParked;
	}
	
	public String toString() {
		return sensorID + " " + occupied + " " + occupiedStateDuration + " " + nearestAvailableID + " " + legallyParked;
	}
}

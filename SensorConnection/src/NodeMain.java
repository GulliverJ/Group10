import database.DatabaseConnect;

public class NodeMain {
	
	public static final int nodeID = 1;
	private static DatabaseConnect dbLink = new DatabaseConnect();
	
	public static void main(String[] args) {
		
		// Initialise this node's child sensors
		SensorRead bay1 = new SensorRead(1, "COM7");
		
		//Continuously feed back results to the database
		while( true ) {
			bay1.updateRawData();
			System.out.println("bay1 Updated");
			try {
			Thread.sleep(60000);					//Waits for a minute
			updateStatus(true);
			} catch (InterruptedException e) {
				System.out.println("Exception caught in NodeMain");
				updateStatus(false);
			}
		}
			
	}
	
	private static void updateStatus(boolean status) {
		String sqlString = "UPDATE nodes SET status = '" + status + "' WHERE nodeID = " + nodeID;
		dbLink.sendStatement(sqlString);
	}

}
	
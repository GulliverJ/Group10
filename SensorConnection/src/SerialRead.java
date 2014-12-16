
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;


public class SerialRead implements SerialPortEventListener {
	SerialPort serialPort;

	private String portToRead = null;
	
	public int readResult = -3;
	
	private BufferedReader input;
	private OutputStream output;
	
	/* Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/* Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	
	public SerialRead(String portToRead) {
		this.portToRead = portToRead;
	}
	
	
	public void initialize() {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(portToRead)) {		//used to be portName
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.disableReceiveTimeout();
			serialPort.enableReceiveThreshold(1);

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/* Closes the port after use */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/* Runs when a serial event occurs (such as when it reads a return from the Arduino) */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = null;

				if (input.ready()) {
					
					inputLine = input.readLine();	// Ignores initial "Start" connection string
					output.write(1);
					output.flush();					// Sends a byte of data to initiate arduino reading
					inputLine = input.readLine();
					
					System.out.println(inputLine);
					close();
					readResult = Integer.parseInt(inputLine); // Converts the read string value into an integer value
					
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
}
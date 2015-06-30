package discoverylab.telebot.master.core;

import discoverylab.telebot.master.configuration.Config;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Handles Serial connections and 
 * @author Irvin Steve Cardenas
 *
 */
public class CoreSerialManager {
	private SerialPort	serialPort;
	
	protected Boolean serialConnected		= false;
	protected Boolean serialPortsAvailable 	= false;
	
	private String 		serialPortName;
	private Integer 	baudRate;
	private Integer 	dataBits;
	private Integer 	stopBits;
	private Integer 	parityType;
	private Integer		eventMask;
	
	/**
	 * Default Constructor use when we want to use 
	 * a default baudrate of 57600 with the default serial parameters
	 * @param serialPortName
	 */
	public CoreSerialManager(String serialPortName) {
		this.serialPortName = serialPortName;
		this.baudRate		= Config.SERIAL_BAUD_RATE;
		this.dataBits 		= Config.SERIAL_DATA_BITS;
		this.stopBits 		= Config.SERIAL_STOP_BITS;
		this.parityType 	= Config.SERIAL_PARITY_TYPE;
		this.eventMask		= Config.SERIAL_EVENT_MASK;
		serialPort 			= new SerialPort(serialPortName);
	}
	
	/**
	 * Use this constructor when we are passing in a String serial port name 
	 * and a String baud rate from the GUI
	 * @param serialPortName
	 * @param baudRate
	 */
	public CoreSerialManager(String serialPortName, String baudRate) {
		this.serialPortName = serialPortName;
		this.baudRate		= Integer.parseInt(baudRate);
		this.dataBits 		= Config.SERIAL_DATA_BITS;
		this.stopBits 		= Config.SERIAL_STOP_BITS;
		this.parityType 	= Config.SERIAL_PARITY_TYPE;
		this.eventMask		= Config.SERIAL_EVENT_MASK;
		serialPort 			= new SerialPort(serialPortName);
	}
	
	/**
	 * Use this constructor when you are feeling SAUCY, are working with a GUI and need specific serial parameters.
	 * The only difference between regular and GUI constructor is the baudrate as a String
	 * @param serialPortName
	 * @param baudRate
	 * @param dataBits
	 * @param stopBits
	 * @param parityType
	 * @param eventMask
	 */
	public CoreSerialManager(String serialPortName, String baudRate, int dataBits, int stopBits, int parityType, int eventMask) {
		this.serialPortName = serialPortName;
		this.baudRate		= Integer.parseInt(baudRate);
		this.dataBits 		= dataBits;
		this.stopBits 		= stopBits;
		this.parityType 	= parityType;
		this.eventMask		= eventMask;
		serialPort 			= new SerialPort(serialPortName);
	}
	
	/**
	 * All time pro constructor, full control. 
	 * You should not be required to use this constructor often expect for special use cases
	 * @param serialPortName
	 * @param baudRate
	 * @param dataBits
	 * @param stopBits
	 * @param parityType
	 * @param eventMask
	 */
	public CoreSerialManager(String serialPortName, int baudRate, int dataBits, int stopBits, int parityType, int eventMask) {
		this.serialPortName = serialPortName;
		this.baudRate		= baudRate;
		this.dataBits 		= dataBits;
		this.stopBits 		= stopBits;
		this.parityType 	= parityType;
		this.eventMask		= eventMask;
		serialPort 			= new SerialPort(serialPortName);
	}
	
	/**
	 * Opens serial port returns true if successful, false otherwise
	 * @return
	 */
	public boolean openPort(){
		try {
			serialPort.openPort();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serialPort.isOpened();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean serialPortClose(){
		try {
			serialPort.removeEventListener();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// We negate because is it's open, that means that it is serialPortClose failed
		return !serialPort.isOpened(); 
	}
	
	/**
	 * Adds (attaches) an event listener to the serial port
	 * @param listener
	 */
	public boolean addEventListener(CoreSerialReader listener){
		//TODO Make it look prettier
		try {
			serialPort.addEventListener(listener);
			return true;
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Utility method to check if the user has defined Serial parameters
	 * Not necessary at the moment but required for future implementations
	 * @return
	 */
	private boolean checkSerialParams(){
		return(serialPortName != null && 
				baudRate != null && 
				dataBits != null && 
				stopBits != null && 
				parityType != null && 
				eventMask != null);
	}
}
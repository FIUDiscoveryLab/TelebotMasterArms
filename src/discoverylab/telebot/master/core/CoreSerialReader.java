package discoverylab.telebot.master.core;

import javax.swing.JComponent;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public abstract class CoreSerialReader implements SerialPortEventListener{
	
	private CallbackInterface callbackInterface;
	
	private SerialPort serialPort;		// Serial Port
//	private JComponent jComponent;		// JComponent to use if updating a GUI component, such as JTextField
	
	public CoreSerialReader(SerialPort serialPort, CallbackInterface callbackInterface ){
		this.serialPort			= serialPort;
		this.callbackInterface 	= callbackInterface;
	}
	
/*	public CoreSerialReader(SerialPort serialPort, JComponent jComponent){
		
	}
	*/
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
//		callbackInterface.callback("");
	}
	
	public abstract String parse(String str);
	
	/**
	 * Callback interface to retrieve serial data
	 * Purpose: Rendering serial data in GUI
	 * @author irvin
	 *
	 */
	public interface CallbackInterface{
		public void callback(String data);
	}
}

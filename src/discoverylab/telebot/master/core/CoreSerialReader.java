package discoverylab.telebot.master.core;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public abstract class CoreSerialReader implements SerialPortEventListener{

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract String parse(String str);
}

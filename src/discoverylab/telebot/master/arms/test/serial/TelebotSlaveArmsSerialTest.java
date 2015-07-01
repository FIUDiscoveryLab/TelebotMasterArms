package discoverylab.telebot.master.arms.test.serial;

import jssc.SerialPort;
import jssc.SerialPortException;

public class TelebotSlaveArmsSerialTest {
	public static void main(String [] args ){
		SerialPort serialPort = new SerialPort("/dev/ttyACM0");
		try {
			serialPort.openPort();
			System.out.println("GOOD");
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("== " + serialPort.isOpened());
	}
}
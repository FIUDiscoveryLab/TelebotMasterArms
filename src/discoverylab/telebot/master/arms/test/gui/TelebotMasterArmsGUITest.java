package discoverylab.telebot.master.arms.test.gui;

import  discoverylab.telebot.master.core.CoreSerialReader.SerialEventInterface;

public class TelebotMasterArmsGUITest implements SerialEventInterface  {

	private SerialEventInterface serialEvent;
	
	public TelebotMasterArmsGUITest(){
		
	}
	
	@Override
	public void serialEvent(String data) {
		// TODO Auto-generated method stub
	}
}

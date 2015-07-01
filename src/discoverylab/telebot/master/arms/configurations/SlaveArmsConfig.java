package discoverylab.telebot.master.arms.configurations;

import jssc.SerialPort;

/**
 * SlaveArmsConfig
 * @author Irvin Steve Cardenas
 *
 * Device Specific Configuration
 * This contains the specific serial parameters for a device used to control the arms on the Master side
 *
 */
public class SlaveArmsConfig {
	public static final String 	DEFAULT_SERIAL_PORT_NAME 	=	"/dev/TelebotArms";
	public static final int 	DEFAULT_SERIAL_BAUD_RATE 	=	57600;
	public static final int 	DEFAULT_SERIAL_DATA_BITS 	=	SerialPort.DATABITS_8;
	public static final int		DEFAULT_SERIAL_STOP_BITS 	=	SerialPort.STOPBITS_1;
	public static final int		DEFAULT_SERIAL_PARITY_TYPE 	=	SerialPort.PARITY_NONE;
	public static final int 	DEFAULT_SERIAL_EVENT_MASK	=	SerialPort.MASK_RXCHAR;
}

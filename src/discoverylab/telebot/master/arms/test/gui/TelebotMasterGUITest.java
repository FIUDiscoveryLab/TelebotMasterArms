package discoverylab.telebot.master.arms.test.gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import discoverylab.telebot.master.core.serial.CoreSerialManager;
import discoverylab.telebot.master.core.serial.CoreSerialReader;
import discoverylab.telebot.master.core.serial.CoreSerialReader.CallbackInterface;
import discoverylab.telebot.master.core.serial.SerialReaderImpl;
import jssc.SerialPortList;
import static discoverylab.util.LogUtils.*;

public class TelebotMasterGUITest implements CoreSerialReader.CallbackInterface  {
	
	public static String TAG = makeLogTag("TelebotMasterArmsGUITest");
	
	private CallbackInterface callbackInterface;
	
	private CoreSerialManager serialManager;
	
	private 	JFrame 				frmAnpp;
	private 	JComboBox<String>	comboBoxPort;
	private 	JComboBox<String> 	comboBoxBaud;
	private 	JButton 			btnConnect;

	protected 	Boolean 			serialConnected = false;
	private 	Boolean 			serialPortsAvailable = false;
	private 	JTextArea 			textArea;
	private 	JPanel 				panel;
	private 	JLabel 				lblLatitude;
	private 	JLabel 				lblLongitude;
	private 	JLabel 				lblHeight;
	protected 	JTextField 			textFieldLatitude;
	protected 	JTextField 			textFieldLongitude;
	protected 	JTextField 			textFieldHeight;
	private 	JLabel 				lblRoll;
	private 	JLabel 				lblPitch;
	private 	JLabel 				lblYaw;
	protected 	JTextField 			textFieldRoll;
	protected 	JTextField 			textFieldPitch;
	protected 	JTextField 			textFieldYaw;


	/**
	 * Create the application
	 */
	public TelebotMasterGUITest(){
		initialize();
	}
	
	/**
	 * Launch the GUI
	 * @param args
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TelebotMasterGUITest window = new TelebotMasterGUITest();
					window.frmAnpp.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void callback(String data) {
		
		SwingUtilities.invokeLater(new Runnable()
        {
            @Override public void run() {
            	textArea.append(data);
           }
        });
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		
		callbackInterface = this;	// Used to reference the interface
		
		// this sets swing to use the native look and feel, much more
		// aesthetically pleasing
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		// all of the swing components and layout management is best edited
		// using the windowbuilder GUI
		frmAnpp = new JFrame();
		frmAnpp.setTitle("Master GUI - Component");
		frmAnpp.setBounds(100, 100, 819, 631);
		frmAnpp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 0, 0, 90, 5, 0 };
		gridBagLayout.rowHeights = new int[] { 10, 0, 0, 0, 5, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmAnpp.getContentPane().setLayout(gridBagLayout);

		comboBoxPort = new JComboBox<String>();
		comboBoxPort.setEnabled(false);
		comboBoxPort.setModel(new DefaultComboBoxModel<String>(new String[] { "No Serial Ports" }));
		GridBagConstraints gbc_comboBoxPort = new GridBagConstraints();
		gbc_comboBoxPort.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxPort.gridx = 1;
		gbc_comboBoxPort.gridy = 1;
		frmAnpp.getContentPane().add(comboBoxPort, gbc_comboBoxPort);

		comboBoxBaud = new JComboBox<String>();
		comboBoxBaud.setModel(new DefaultComboBoxModel<String>(new String[] { "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "921600", "1000000" }));
		comboBoxBaud.setSelectedIndex(7);
		GridBagConstraints gbc_comboBoxBaud = new GridBagConstraints();
		gbc_comboBoxBaud.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxBaud.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxBaud.gridx = 2;
		gbc_comboBoxBaud.gridy = 1;
		frmAnpp.getContentPane().add(comboBoxBaud, gbc_comboBoxBaud);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		
		btnConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// if the serial port is not connected, this is a connect action
				// if the serial port is connected, this is a disconnect action
				if (!serialConnected)
				{
					try
					{
						// STEP 1: Create Serial Manager
						serialManager = new CoreSerialManager(
									(String) comboBoxPort.getSelectedItem()
								, 	(String) comboBoxBaud.getSelectedItem());
						
						// STEP 2: Open Serial Port
						if(serialManager.openPort()){
							LOGI(TAG, "Serial Manager opened serial port");
						}
						else {
							LOGI(TAG, "Serial Manager failed to open serial port");
						}
						
						// STEP 3: Initialize Serial Paramaters
						if(serialManager.initialize()) {
							LOGI(TAG, "Serial Manager initialized ports");
						}
						
//						serialPort.setParams(Integer.parseInt((String) comboBoxBaud.getSelectedItem()), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//						serialPort.setEventsMask(SerialPort.MASK_RXCHAR);

						// STEP 4: Add Serial Event Listener
						serialManager.addEventListener(
								new SerialReaderImpl(serialManager.getSerialPort(), callbackInterface));

						// change the button to be disconnect once we are
						// connected
						btnConnect.setText("Disconnect");
						serialConnected = true;
						comboBoxPort.setEnabled(false);
						comboBoxBaud.setEnabled(false);
					}
					catch (Exception exception)
					{
						System.err.println(exception.toString());
						serialManager.serialPortClose();
					}
				}
				else
				{
					serialManager.serialPortClose();
					
					btnConnect.setText("Connect");
					serialConnected = false;
					comboBoxPort.setEnabled(true);
					comboBoxBaud.setEnabled(true);
				}
			}
		});
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 3;
		gbc_btnConnect.gridy = 1;
		frmAnpp.getContentPane().add(btnConnect, gbc_btnConnect);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		frmAnpp.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 10, 0, 0, 5, 0, 5, 0, 0 };
		gbl_panel.rowHeights = new int[] { 5, 0, 0, 0, 5, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		/////////////////////////////////////////////////////////////////////////////////////////////////
		// ROW 1
		lblLatitude = new JLabel("VAL_0:");
		GridBagConstraints gbc_lblLatitude = new GridBagConstraints();
		gbc_lblLatitude.anchor = GridBagConstraints.EAST;
		gbc_lblLatitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblLatitude.gridx = 1;
		gbc_lblLatitude.gridy = 1;
		panel.add(lblLatitude, gbc_lblLatitude);

		textFieldLatitude = new JTextField();
		GridBagConstraints gbc_textFieldLatitude = new GridBagConstraints();
		gbc_textFieldLatitude.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldLatitude.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLatitude.gridx = 2;
		gbc_textFieldLatitude.gridy = 1;
		panel.add(textFieldLatitude, gbc_textFieldLatitude);
		textFieldLatitude.setColumns(15);

		lblRoll = new JLabel("VAL_1:");
		GridBagConstraints gbc_lblRoll = new GridBagConstraints();
		gbc_lblRoll.anchor = GridBagConstraints.EAST;
		gbc_lblRoll.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoll.gridx = 4;
		gbc_lblRoll.gridy = 1;
		panel.add(lblRoll, gbc_lblRoll);

		textFieldRoll = new JTextField();
		GridBagConstraints gbc_textFieldRoll = new GridBagConstraints();
		gbc_textFieldRoll.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldRoll.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldRoll.gridx = 5;
		gbc_textFieldRoll.gridy = 1;
		panel.add(textFieldRoll, gbc_textFieldRoll);
		textFieldRoll.setColumns(15);
		// ./ROW 1

		/////////////////////////////////////////////////////////////////////////////////////////////////
		// ROW 2
		lblLongitude = new JLabel("VAL_2");
		GridBagConstraints gbc_lblLongitude = new GridBagConstraints();
		gbc_lblLongitude.anchor = GridBagConstraints.EAST;
		gbc_lblLongitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblLongitude.gridx = 1;
		gbc_lblLongitude.gridy = 2;
		panel.add(lblLongitude, gbc_lblLongitude);

		textFieldLongitude = new JTextField();
		GridBagConstraints gbc_textFieldLongitude = new GridBagConstraints();
		gbc_textFieldLongitude.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldLongitude.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldLongitude.gridx = 2;
		gbc_textFieldLongitude.gridy = 2;
		panel.add(textFieldLongitude, gbc_textFieldLongitude);
		textFieldLongitude.setColumns(15);

		lblPitch = new JLabel("VAL_3:");
		GridBagConstraints gbc_lblPitch = new GridBagConstraints();
		gbc_lblPitch.anchor = GridBagConstraints.EAST;
		gbc_lblPitch.insets = new Insets(0, 0, 5, 5);
		gbc_lblPitch.gridx = 4;
		gbc_lblPitch.gridy = 2;
		panel.add(lblPitch, gbc_lblPitch);

		textFieldPitch = new JTextField();
		GridBagConstraints gbc_textFieldPitch = new GridBagConstraints();
		gbc_textFieldPitch.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPitch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPitch.gridx = 5;
		gbc_textFieldPitch.gridy = 2;
		panel.add(textFieldPitch, gbc_textFieldPitch);
		textFieldPitch.setColumns(15);
		// .ROW 2

		/////////////////////////////////////////////////////////////////////////////////////////////////
		// ROW 2		
		lblHeight = new JLabel("VAL_4");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.anchor = GridBagConstraints.EAST;
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 1;
		gbc_lblHeight.gridy = 3;
		panel.add(lblHeight, gbc_lblHeight);

		textFieldHeight = new JTextField();
		GridBagConstraints gbc_textFieldHeight = new GridBagConstraints();
		gbc_textFieldHeight.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldHeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldHeight.gridx = 2;
		gbc_textFieldHeight.gridy = 3;
		panel.add(textFieldHeight, gbc_textFieldHeight);
		textFieldHeight.setColumns(15);

		lblYaw = new JLabel("VAL_5:");
		GridBagConstraints gbc_lblYaw = new GridBagConstraints();
		gbc_lblYaw.anchor = GridBagConstraints.EAST;
		gbc_lblYaw.insets = new Insets(0, 0, 5, 5);
		gbc_lblYaw.gridx = 4;
		gbc_lblYaw.gridy = 3;
		panel.add(lblYaw, gbc_lblYaw);

		textFieldYaw = new JTextField();
		GridBagConstraints gbc_textFieldYaw = new GridBagConstraints();
		gbc_textFieldYaw.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldYaw.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldYaw.gridx = 5;
		gbc_textFieldYaw.gridy = 3;
		panel.add(textFieldYaw, gbc_textFieldYaw);
		textFieldYaw.setColumns(15);
		// ./ROW 3

		/////////////////////////////////////////////////////////////////////////////////////////////////
		// TEXT AREA
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 3;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 3;
		frmAnpp.getContentPane().add(scrollPane, gbc_textArea);
		// ./TEXT AREA

		// create a thread to update the available serials ports once a second
		// it is done in a new thread because under windows it can block for up
		// to 10 seconds
		Thread portScannerThread = new Thread(new PortScanner());
		portScannerThread.start();
	}

	public class PortScanner implements Runnable
	{
		public void run()
		{
			while (true)
			{
				if (!serialConnected)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							if (!serialConnected)
							{
								String[] portNames = SerialPortList.getPortNames();
								int comboLength = comboBoxPort.getItemCount();
								if (!serialPortsAvailable) comboLength = 0;
								Boolean portsChanged = portNames.length != comboLength;
								if(!portsChanged)
								{
									for(int i = 0; i < portNames.length; i++)
									{
										if(portNames[i].compareTo((String)comboBoxPort.getItemAt(i)) != 0)
										{
											portsChanged = true;
											break;
										}
									}
								}
								if (portsChanged)
								{
									comboBoxPort.removeAllItems();
									for (int i = 0; i < portNames.length; i++)
									{
										comboBoxPort.addItem(portNames[i]);
									}
									if (portNames.length == 0)
									{
										comboBoxPort.addItem("No Serial Ports");
										comboBoxPort.setSelectedIndex(0);
										comboBoxPort.setEnabled(false);
										btnConnect.setEnabled(false);
										serialPortsAvailable = false;
									}
									else
									{
										comboBoxPort.setSelectedIndex(0);
										comboBoxPort.setEnabled(true);
										btnConnect.setEnabled(true);
										serialPortsAvailable = true;
									}
								}
							}
						}
					});
				}
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

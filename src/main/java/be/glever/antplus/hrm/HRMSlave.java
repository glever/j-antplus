package be.glever.antplus.hrm;

import be.glever.ant.AntException;
import be.glever.ant.channel.*;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.usb.AntUsbDevice;

public class HRMSlave {

	private static final byte ANTPLUS_HRM_FREQUENCY = (byte) 57; // 2457Mhz
	private AntUsbDevice antUsbDevice;

	public HRMSlave(AntUsbDevice antUsbDevice) {
		this.antUsbDevice = antUsbDevice;
	}
	
	public AntChannel[] listDevices() throws AntException {
		HRMChannel channel = new HRMChannel();

		// ASSIGN CHANNEL
		AssignChannelMessage assignChannelMessage = new AssignChannelMessage((byte) channel.getChannelType().ordinal(), channel.getChannelType().getValue(), channel.getNetwork().getNumber());
		antUsbDevice.sendMessage(assignChannelMessage );

		// OPEN CHANNEL
		return null;
	}
	
}

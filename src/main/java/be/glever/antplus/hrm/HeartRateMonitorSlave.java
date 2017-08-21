package be.glever.antplus.hrm;

import be.glever.ant.channel.AntChannel;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelType;
import be.glever.ant.channel.AntChannelRfFrequency;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.usb.AntUsbDevice;

public class HeartRateMonitorSlave {

	private static final byte ANTPLUS_HRM_FREQUENCY = (byte) 57; // 2457Mhz
	private AntUsbDevice antUsbDevice;

	public HeartRateMonitorSlave(AntUsbDevice antUsbDevice) {
		this.antUsbDevice = antUsbDevice;
	}
	
	public AntChannel[] listDevices() {
		AntChannel channel = new AntChannel();
		channel.setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
		channel.setNetwork(null); // default network for ant+
		channel.setRfFrequency(new AntChannelRfFrequency(ANTPLUS_HRM_FREQUENCY));
		
		AntChannelId channelId = new AntChannelId();
		channelId.setTransmissionType(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE);
		channelId.setDeviceNumber(new byte[] {0,0});
		channelId.setDeviceType((byte) 120);
		
		
		
		return null;
	}
	
}

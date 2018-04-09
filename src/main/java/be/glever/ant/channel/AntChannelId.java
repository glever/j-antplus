package be.glever.ant.channel;

import be.glever.ant.constants.AntPlusDeviceType;

public class AntChannelId {

	private AntChannelTransmissionType transmissionType;
	private byte deviceType; // set by master device, 0 for slave devices. TODO MSB is a pairing bit. split device type from pairing bit?
	private AntDeviceNumber deviceNumber; // TODO remember this is little endian

	public AntChannelId() {
	}

	public AntChannelId(AntChannelTransmissionType transmissionType, AntPlusDeviceType deviceType, AntDeviceNumber deviceNumber) {
		this.transmissionType = transmissionType;
		this.deviceType = deviceType.value();
		this.deviceNumber = deviceNumber;
	}

	public AntChannelTransmissionType getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(AntChannelTransmissionType transmissionType) {
		this.transmissionType = transmissionType;
	}

	public byte getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}

	public AntDeviceNumber getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(AntDeviceNumber deviceNumber) {
		this.deviceNumber = deviceNumber;
	}


}

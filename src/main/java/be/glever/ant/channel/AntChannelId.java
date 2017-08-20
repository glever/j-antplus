package be.glever.ant.channel;

public class AntChannelId {

	private AntChannelTransmissionType transmissionType;
	private byte deviceType; // set by master device, 0 for slave devices. TODO MSB is a pairing bit. split device type from pairing bit?
	private byte[] deviceNumber; // TODO remember this is little endian
	
	public AntChannelId() {
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

	public byte[] getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(byte[] deviceNumber) {
		if(deviceNumber.length != 2) {
			throw new IllegalArgumentException("Device number should be 16 bits");
		}
		this.deviceNumber = deviceNumber;
	}
	
	
}

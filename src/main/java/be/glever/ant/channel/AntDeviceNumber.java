package be.glever.ant.channel;

import be.glever.ant.util.ByteUtils;

import java.util.Arrays;

public class AntDeviceNumber {
	public static final AntDeviceNumber DEVICE_NUMBER_FOR_SEARCHING = new AntDeviceNumber(new byte[]{0,0});

	private byte[] deviceNumber;

	public AntDeviceNumber(byte[] deviceNumber) {
		if (deviceNumber.length != 2) {
			throw new IllegalArgumentException("AntDeviceNumber must be 2 bytes long. Given was: " + ByteUtils.hexString(deviceNumber));
		}
		this.deviceNumber = deviceNumber;
	}

	public byte[] getBytes(){
		return Arrays.copyOf(this.deviceNumber, this.deviceNumber.length);
	}
}

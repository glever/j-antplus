package be.glever.ant.constants;

public enum AntDeviceType {
	HRM((byte) 0x78);

	private byte value;

	AntDeviceType(byte i) {
		value = i;
	}

	public byte value() {
		return value;
	}
}

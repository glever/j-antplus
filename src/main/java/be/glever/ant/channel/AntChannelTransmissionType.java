package be.glever.ant.channel;

public class AntChannelTransmissionType {
	private byte value;
	
	/**
	 * Use this one for searching devices. It is the Master device that decides the TransmissionType, which slaves must use when opening a channel. 
	 */
	public static final AntChannelTransmissionType PAIRING_TRANSMISSION_TYPE = new AntChannelTransmissionType((byte)0,(byte)0,(byte)0);

	public AntChannelTransmissionType(byte value) {
		this.value = value;
	}

	
	public AntChannelTransmissionType(byte sharedAddressIndicator, byte globalDataPagesUsage, byte deviceNumberExtension) {
		sharedAddressIndicator = (byte) (0b11 & sharedAddressIndicator);
		globalDataPagesUsage = (byte) (0b100 & (globalDataPagesUsage << 2));
		// 4th bit unused
		deviceNumberExtension = (byte) (deviceNumberExtension << 4);

		this.value = (byte) (sharedAddressIndicator | globalDataPagesUsage | deviceNumberExtension);
	}

	public byte getSharedAddressIndicator() {
		return (byte) (value & 0b11);
	}

	public byte getGlobalDataPagesUsage() {
		return (byte) ((value & 0b100) >> 2);
	}

	public byte getOptionalDeviceNumberExtension() {
		return (byte) (value >> 4);
	}

	public interface SharedAddressIndicator {
		byte RESERVED = 0;
		byte INDEPENDENT = 1;
		byte SHARED_WITH_1_BYTE_ADDRESS = 0b10;
		byte SHARED_WITH_2_BYTE_ADDRESS = 0b11;
	}

	public interface GlobalDataPagesUsage {
		byte NOT_USED = 0;
		byte USED = 1;
	}

	public byte getValue() {
		return value;
	}
}

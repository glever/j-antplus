package be.glever.ant.constants;
public enum AntChannelType{
	BIDIRECTIONAL_SLAVE(0x00),
	BIDIRECTIONAL_MASTER(0x10),
	SHARED_BIDRECTIONAL_SLAVE(0x20),
	SHARED_BIDIRECTIONAL_MASTER(0x30),
	SLAVE_RECEIVE_ONLY(0x40),
	MASTER_TRANSMIT_ONLY(0x50);
	
	private byte value;
	
	private AntChannelType(int value) {
		this.value = (byte) value;
	}
	
	public AntChannelType fromValue(byte value) {
		for(AntChannelType ct : AntChannelType.values()) {
			if(ct.value == value) {
				return ct;
			}
		}
		throw new IllegalArgumentException("Unknown channel type for value [" + value + "]");
	}

	public byte getValue() {
		return value;
	}
}
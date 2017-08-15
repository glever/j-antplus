package be.glever.ant.channel;
public enum ChannelType{
	BIDIRECTIONAL_SLAVE(0x00),
	BIDIRECTIONAL_MASTER(0x10),
	SHARED_BIDRECTIONAL_SLAVE(0x20),
	SHARED_BIDIRECTIONAL_MASTER(0x30),
	SLAVE_RECEIVE_ONLY(0x40),
	MASTER_TRANSMIT_ONLY(0x50);
	
	private byte value;
	
	private ChannelType(int value) {
		this.value = (byte) value;
	}
	
	public ChannelType fromValue(byte value) {
		for(ChannelType ct : ChannelType.values()) {
			if(ct.value == value) {
				return ct;
			}
		}
		throw new IllegalArgumentException("Unknown channel type for value [" + value + "]");
	}
}
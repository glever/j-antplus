package be.glever.ant.channel;

public class ChannelId {

	private TransmissionType transmissionType;
	private byte deviceType; // set by master device, 0 for slave devices. TODO MSB is a pairing bit. split device type from pairing bit?
	private byte[] deviceNumber; // TODO remember this is little endian
}

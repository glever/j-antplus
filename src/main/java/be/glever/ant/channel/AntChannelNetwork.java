package be.glever.ant.channel;

import be.glever.ant.constants.AntNetworkKeys;

public class AntChannelNetwork {
	private byte number = 0;
	private byte[] keyBytes;

	public AntChannelNetwork() {
	}

	public AntChannelNetwork(byte number, AntNetworkKeys key) {
		this.number = number;
		this.keyBytes = key.getBytes();
	}

	public byte getNumber() {
		return number;
	}

	public void setNumber(byte number) {
		this.number = number;
	}

	public byte[] getKeyBytes() {
		return keyBytes;
	}

	public void setKeyBytes(byte[] keyBytes) {
		this.keyBytes = keyBytes;
	}

}

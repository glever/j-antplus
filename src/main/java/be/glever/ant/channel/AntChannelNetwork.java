package be.glever.ant.channel;

public class AntChannelNetwork {
	private byte number = 0;
	private byte[] key;

	public AntChannelNetwork() {
	}

	public byte getNumber() {
		return number;
	}

	public void setNumber(byte number) {
		this.number = number;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

}

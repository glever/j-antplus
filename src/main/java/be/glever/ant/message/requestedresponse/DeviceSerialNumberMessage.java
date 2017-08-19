package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class DeviceSerialNumberMessage extends AbstractAntMessage {

	private byte[] bytes;

	@Override
	public byte getMessageId() {
		return 0x61;
	}

	@Override
	public byte[] getMessageContent() {
		return bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = bytes;
	}

	public DeviceSerialNumberMessage(byte[] serialNumber) {
		this.bytes = serialNumber;
	}

	public byte[] getSerialNumber() {
		return this.bytes;
	}

}

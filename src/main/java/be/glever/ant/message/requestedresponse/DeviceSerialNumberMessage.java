package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class DeviceSerialNumberMessage extends AbstractAntMessage {

	public static final byte MSG_ID = 0x61;
	private byte[] bytes;

	public DeviceSerialNumberMessage() {
	}

	@Override
	public byte getMessageId() {
		return MSG_ID;
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

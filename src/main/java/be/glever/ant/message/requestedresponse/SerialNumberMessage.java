package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class SerialNumberMessage extends AbstractAntMessage {

	public static final byte MSG_ID = 0x61;
	private byte[] bytes;

	public SerialNumberMessage() {
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
		this.bytes = messageContentBytes;
	}

	public SerialNumberMessage(byte[] serialNumber) {
		this.bytes = serialNumber;
	}

	public byte[] getSerialNumber() {
		return this.bytes;
	}

}

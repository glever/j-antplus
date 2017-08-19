package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class RequestMessage extends AbstractAntMessage {

	public static final byte MSG_ID = 0x4d;

	private byte[] bytes;

	public RequestMessage() {
	}

	public RequestMessage(byte channelNumberOrSubMessageId, byte msgIdRequested, byte addr, byte size) {
		this.bytes = new byte[] { channelNumberOrSubMessageId, msgIdRequested, addr, size };
	}

	@Override
	public byte getMessageId() {
		return MSG_ID;
	}

	@Override
	public byte[] getMessageContent() {
		return this.bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

	public byte getMsgIdRequested() {
		return bytes[1];
	}
}

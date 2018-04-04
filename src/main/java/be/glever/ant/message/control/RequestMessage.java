package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class RequestMessage extends AbstractAntMessage  implements AntBlockingMessage {

	public static final byte MSG_ID = 0x4d;

	private byte[] bytes;

	public RequestMessage() {
	}

	public RequestMessage(byte channelNumberOrSubMessageId, byte msgIdRequested, Byte addr, Byte size) {
		this.bytes = new ByteArrayBuilder().write(channelNumberOrSubMessageId, msgIdRequested).writeIfNotNull(addr)
				.writeIfNotNull(size).toByteArray();
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

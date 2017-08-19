package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class AntVersionMessage extends AbstractAntMessage {

	private byte[] bytes;

	@Override
	public byte getMessageId() {
		return 0x3e;
	}

	public AntVersionMessage(byte[] versionMessage) {
		this.bytes = versionMessage;
	}
	@Override
	public byte[] getMessageContent() {
		return bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}
	public byte[] getAntVersion() {
		return bytes;
	}

}

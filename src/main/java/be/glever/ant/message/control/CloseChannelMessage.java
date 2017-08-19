package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class CloseChannelMessage extends AbstractAntMessage {

	private byte[] bytes;

	public CloseChannelMessage() {
	}

	public CloseChannelMessage(byte channelNr) {
		this.bytes = new byte[] { channelNr };
	}

	@Override
	public byte getMessageId() {
		return 0x4c;
	}

	@Override
	public byte[] getMessageContent() {
		return this.bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

	public byte getChannelNr() {
		return bytes[0];
	}

}

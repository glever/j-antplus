package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class OpenChannelMessage extends AbstractAntMessage {

	private byte[] bytes;

	public OpenChannelMessage(byte channelNr) {
		this.bytes = new byte[] {channelNr};
	}
	@Override
	public byte getMessageId() {
			return 0x4b;
	}

	@Override
	public byte[] getMessageContent() {
		return bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

	public byte getChannelNr() {
		return bytes[0];
	}
}

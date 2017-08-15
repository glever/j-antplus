package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class UnassignChannelMessage extends AbstractAntMessage {

	private byte channelNumber;

	public UnassignChannelMessage(byte channelNumber) {
		this.channelNumber = channelNumber;
	}

	public UnassignChannelMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x41;
	}

	@Override
	public byte[] getMessageContent() {
		return new byte[] { channelNumber };
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub
		
	}
}

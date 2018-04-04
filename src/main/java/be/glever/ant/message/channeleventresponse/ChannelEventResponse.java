package be.glever.ant.message.channeleventresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class ChannelEventResponse extends AbstractAntMessage {
	private byte[] messageContentBytes;

	@Override
	public byte[] getMessageContent() {
		return new byte[0];
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.messageContentBytes = messageContentBytes;
	}

	@Override
	public byte getMessageId() {
		return 0x40;
	}

	public byte getChannelNumber(){
		return messageContentBytes[0];
	}

	public byte getRespondToMessageId(){
		return messageContentBytes[1];
	}

	public ChannelEvenResponseCode getResponseCode(){
		return ChannelEvenResponseCode.fromValue(messageContentBytes[2]);
	}
}

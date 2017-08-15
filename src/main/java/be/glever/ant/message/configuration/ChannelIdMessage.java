package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class ChannelIdMessage extends AbstractAntMessage {

	private byte channelNumber;
	private byte[] deviceNumber;
	private byte deviceTypeId;
	private byte transactionType;

	public ChannelIdMessage(byte channelNumber, byte[] deviceNumber, byte deviceTypeId, byte transactionType) {
		this.channelNumber = channelNumber;
		this.deviceNumber = deviceNumber;
		this.deviceTypeId = deviceTypeId;
		this.transactionType = transactionType;
	}

	public ChannelIdMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x51;
	}

	@Override
	public byte[] getMessageContent() {
		return new ByteArrayBuilder()
				.write(channelNumber)
				.write(deviceNumber)
				.write(deviceTypeId)
				.write(transactionType)
				.toByteArray();
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub
		
	}

}

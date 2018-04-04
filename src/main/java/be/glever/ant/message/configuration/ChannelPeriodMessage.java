package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class ChannelPeriodMessage extends AbstractAntMessage  implements AntBlockingMessage {

	private byte channelNumber;
	private byte rfFrequency;

	public ChannelPeriodMessage(byte channelNumber, byte rfFrequency) {
		this.channelNumber = channelNumber;
		this.rfFrequency = rfFrequency;
	}

	public ChannelPeriodMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x45;
	}

	@Override
	public byte[] getMessageContent() {
		return new ByteArrayBuilder()
				.write(channelNumber)
				.write(rfFrequency)
				.toByteArray();
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub
		
	}

}

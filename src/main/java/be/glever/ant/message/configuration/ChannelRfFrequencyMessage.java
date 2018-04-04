package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class ChannelRfFrequencyMessage extends AbstractAntMessage  implements AntBlockingMessage {

	private byte channelNumber;
	private byte[] channelPeriod;

	public ChannelRfFrequencyMessage(byte channelNumber, byte[] channelPeriod) {
		this.channelNumber = channelNumber;
		this.channelPeriod = channelPeriod;
	}

	public ChannelRfFrequencyMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x43;
	}

	@Override
	public byte[] getMessageContent() {
		return new ByteArrayBuilder()
				.write(channelNumber)
				.write(channelPeriod)
				.toByteArray();
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub
		
	}

}

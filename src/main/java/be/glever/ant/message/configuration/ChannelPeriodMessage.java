package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class ChannelPeriodMessage extends AbstractAntMessage implements AntBlockingMessage {

	private byte channelNumber;
	private byte[] period;

	public ChannelPeriodMessage(byte channelNumber, byte period[]) {
		this.channelNumber = channelNumber;
		this.period = period;
	}

	public ChannelPeriodMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x43;
	}

	@Override
	public byte[] getMessageContent() {
		return new ByteArrayBuilder()
				.write(channelNumber)
				.write(period)
				.toByteArray();
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub

	}

}

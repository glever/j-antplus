package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class AssignChannelMessage extends AbstractAntMessage implements AntBlockingMessage {

	private byte channelNumber;
	private byte channelType;
	private byte networkNumber;
	private Byte extendedAssignment; // optional

	public AssignChannelMessage(byte channelNumber, byte channelType, byte networkNumber, Byte extendedAssignment) {
		this.channelNumber = channelNumber;
		this.channelType = channelType;
		this.networkNumber = networkNumber;
		this.extendedAssignment = extendedAssignment;
	}

	public AssignChannelMessage(byte channelNumber, byte channelType, byte networkNumber) {
		this(channelNumber, channelType, networkNumber, null);
	}

	public AssignChannelMessage() {
	}

	@Override
	public byte getMessageId() {
		return 0x42;
	}

	@Override
	public byte[] getMessageContent() {
		ByteArrayBuilder bb = new ByteArrayBuilder()
				.write(channelNumber)
				.write(channelType)
				.write(networkNumber)
				.write(extendedAssignment == null ? 0x00 : extendedAssignment);

		return bb.toByteArray();
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		// TODO Auto-generated method stub

	}
}

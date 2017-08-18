package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class ChannelStatusMessage extends AbstractAntMessage {

	private byte[] bytes;

	@Override
	public byte getMessageId() {
		return 0x52;
	}

	@Override
	public byte[] getMessageContent() {
		return bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

	public byte getChannelNumber() {
		return this.bytes[0];
	}

	public static enum CHANNEL_STATUS {
		UnAssigned, Assigned, Searching, Tracking;

		public static CHANNEL_STATUS fromValue(byte value) {
			switch (value & 3) {
			case 0:
				return UnAssigned;
			case 1:
				return Assigned;
			case 2:
				return Searching;
			case 3:
				return Tracking;
			default:
				throw new IllegalArgumentException("Bad value passed");
			}
		}
	}
}

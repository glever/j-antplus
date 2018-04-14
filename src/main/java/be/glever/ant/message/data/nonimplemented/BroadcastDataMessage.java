package be.glever.ant.message.data.nonimplemented;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

/**
 * Base class for all broadcast messages (ie data payloads received from ant+ devices).
 * TODO when needed, extend this class further to account for extended data behavior as defined in 7.1.1
 */
public class BroadcastDataMessage extends AbstractAntMessage {
	private byte[] messageContentBytes;

	@Override
	public byte[] getMessageContent() {
		return messageContentBytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.messageContentBytes = messageContentBytes;
	}

	@Override
	public byte getMessageId() {
		return 0x4e;
	}


}

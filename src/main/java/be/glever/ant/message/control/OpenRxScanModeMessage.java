package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;

public class OpenRxScanModeMessage extends AbstractAntMessage implements AntBlockingMessage {

	private byte[] bytes;

	public OpenRxScanModeMessage() {
	}

	public OpenRxScanModeMessage(boolean synchronousPacketsOnly) {
		this.bytes = new byte[] { 0, (byte) (synchronousPacketsOnly ? 1 : 0) };
	}

	@Override
	public byte getMessageId() {
		return 0x5b;
	}

	@Override
	public byte[] getMessageContent() {
		return this.bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

}

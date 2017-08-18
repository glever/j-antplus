package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class OpenRxScanMode extends AbstractAntMessage {

	private byte[] bytes;

	public OpenRxScanMode(boolean synchronousPacketsOnly) {
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

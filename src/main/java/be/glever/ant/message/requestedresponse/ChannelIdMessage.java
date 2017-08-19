package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class ChannelIdMessage extends AbstractAntMessage {

	private byte[] bytes;

	public ChannelIdMessage(byte channelNr, byte[] deviceNr, byte deviceTypeID, byte transmissionType) {
		this.bytes = new byte[] {channelNr, deviceNr[0], deviceNr[1], deviceTypeID, transmissionType};
	}
	public byte getChannelNr() {
		return bytes[0];
	}
	public byte[]getDeviceNr(){
		return new byte[] {bytes[1], bytes[2]};
	}
	public byte getDeviceTypeId() {
		return bytes[3];
	}
	public byte  getTransmissionType() {
		return bytes[4];
	}

	@Override
	public byte getMessageId() {
		return 0x51;
	}

	@Override
	public byte[] getMessageContent() {
		return bytes;
	}

	@Override
	public void setMessageBytes(byte[] messageContentBytes) throws AntException {
		this.bytes = messageContentBytes;
	}

}

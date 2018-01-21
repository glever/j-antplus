package be.glever.ant.message;

import java.util.Arrays;

import be.glever.ant.AntException;
import be.glever.ant.util.ByteUtils;

public abstract class AbstractAntMessage implements AntMessage {

	private byte sync = (byte) 0xa4; // TODO synchronous mode (0xa5 used for WRITE mode) is currently unsupported

	public abstract byte[] getMessageContent();

	public abstract void setMessageBytes(byte[] messageContentBytes) throws AntException;

	@Override
	public byte[] toByteArray() {
		byte[] bytes = new byte[8];
		byte[] messageContent = getMessageContent();

		int idx = 0;
		bytes[idx++] = sync;

		bytes[idx++] = (byte) messageContent.length;
		bytes[idx++] = getMessageId();

		for (byte bite : messageContent) {
			bytes[idx++] = bite;
		}
		bytes[idx++] = getCheckSum(bytes, idx);

		return bytes;
	}

	private byte getCheckSum(byte[] bytes, int idx) {
		byte checksum = bytes[0];
		for (int i = 1; i < idx; i++) {
			checksum ^= bytes[i];
		}
		return checksum;
	}

	@Override
	public void parse(byte[] bytes) throws AntException {
		validateNumberDataBytes(bytes);
		validateChecksum(bytes);
		setMessageBytes(Arrays.copyOfRange(bytes, 3, bytes.length - 1));
	}

	private void validateChecksum(byte[] bytes) throws AntException {
		byte calculatedChecksum = bytes[0];
		for (int i = 1; i < bytes.length - 1; i++) {
			calculatedChecksum ^= bytes[i];
		}

		byte checkSum = bytes[bytes.length - 1];
		if (calculatedChecksum != checkSum) {
			throw new AntException("Checksum doesnt match. Given: [" + checkSum + "]. Calculated: ["
					+ calculatedChecksum + "]. Message: " + ByteUtils.hexString(bytes));
		}
	}

	private void validateNumberDataBytes(byte[] bytes) throws AntException {
		byte nrDataBytes = bytes[1];
		if (nrDataBytes + 4 != bytes.length) {
			throw new AntException("Incorrect message length given [" + nrDataBytes + "] for byte array "
					+ ByteUtils.hexString(bytes));
		}
	}

}

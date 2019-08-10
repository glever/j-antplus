package be.glever.ant.message;

import be.glever.ant.AntException;
import be.glever.ant.util.ByteArrayBuilder;
import be.glever.ant.util.ByteUtils;

import java.util.Arrays;

public abstract class AbstractAntMessage implements AntMessage {

    public abstract byte[] getMessageContent();

    public abstract void setMessageBytes(byte[] messageContentBytes) throws AntException;

    @Override
    public byte[] toByteArray() {
        byte[] messageContent = getMessageContent();

        ByteArrayBuilder bab = new ByteArrayBuilder();
        // TODO synchronous mode (0xa5 used for WRITE mode) is currently unsupported
        byte sync = (byte) 0xa4;
        bab.write(sync);
        bab.write((byte) messageContent.length);
        bab.write(getMessageId());
        bab.write(messageContent);
        bab.write(getCheckSum(bab.toByteArray()));
        bab.write((byte) 0x00, (byte) 0x00); // recommendation

        return bab.toByteArray();
    }

    @Override
    public void parse(byte[] bytes) throws AntException {
        validateNumberDataBytes(bytes);
        validateChecksum(bytes);
        setMessageBytes(Arrays.copyOfRange(bytes, 3, bytes.length - 1));
    }

    private byte getCheckSum(byte[] bytes) {
        byte checksum = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            checksum ^= bytes[i];
        }
        return checksum;
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

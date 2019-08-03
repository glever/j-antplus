package be.glever.ant.message.notification;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.util.ByteUtils;

public class SerialErrorMessage extends AbstractAntMessage {
    public static final byte MSG_ID = (byte) 0xae;
    private byte[] bytes;

    public SerialErrorMessage() {
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte[] getMessageContent() {
        return bytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.bytes = messageContentBytes;
    }

    public boolean isFirstByteNotSync() {
        return ByteUtils.hasMask(bytes[0], 0);
    }

    public boolean isCheckSumIncorrect() {
        return ByteUtils.hasMask(bytes[0], 2);
    }

    public boolean isAntMessageTooLarge() {
        return ByteUtils.hasMask(bytes[0], 3);
    }
}

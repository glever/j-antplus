package be.glever.ant.message.data;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.util.ByteUtils;

import java.util.Arrays;

/**
 * Base class for all broadcast messages (ie data payloads received from ant+ devices).
 * TODO when needed, extend this class further to account for extended data behavior as defined in 7.1.1
 */
public class BroadcastDataMessage extends AbstractAntMessage {
    public static final int MSG_ID = 0x4e;
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
        return MSG_ID;
    }

    public byte getChannelNumber() {
        return getMessageContent()[0];
    }

    public byte[] getPayLoad() {
        return Arrays.copyOfRange(messageContentBytes, 1, messageContentBytes.length);
    }

    @Override
    public String toString() {
        return "BroadcastDataMessage{" +
                "messageContentBytes=" + ByteUtils.hexString(messageContentBytes) +
                '}';
    }


}

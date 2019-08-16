package be.glever.ant.message.data;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.util.ByteUtils;

import java.util.Arrays;

public class AchnowledgeDataMessage extends AbstractAntMessage {

    public static final int MSG_ID = 0x4f;
    private byte[] messageContentBytes;

    public AchnowledgeDataMessage() {
        super();
    }

    public AchnowledgeDataMessage(byte channelNumber, byte requestedDatapage) {

    }

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

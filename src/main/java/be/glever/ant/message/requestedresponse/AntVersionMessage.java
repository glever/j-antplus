package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class AntVersionMessage extends AbstractAntMessage {

    public static final byte MSG_ID = 0x3e;
    private byte[] bytes;

    public AntVersionMessage() {
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    public AntVersionMessage(byte[] versionMessage) {
        this.bytes = versionMessage;
    }

    @Override
    public byte[] getMessageContent() {
        return bytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.bytes = messageContentBytes;
    }

    public byte[] getAntVersion() {
        return bytes;
    }

}

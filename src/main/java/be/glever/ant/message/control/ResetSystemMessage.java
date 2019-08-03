package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

public class ResetSystemMessage extends AbstractAntMessage {

    public ResetSystemMessage() {
    }

    @Override
    public byte getMessageId() {
        return 0x4a;
    }

    @Override
    public byte[] getMessageContent() {
        return new byte[]{0};
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        // empty
    }

}

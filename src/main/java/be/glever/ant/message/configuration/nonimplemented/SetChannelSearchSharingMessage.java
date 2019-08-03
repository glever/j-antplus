package be.glever.ant.message.configuration.nonimplemented;

import be.glever.ant.AntException;
import be.glever.ant.message.AntBlockingMessage;

public class SetChannelSearchSharingMessage implements AntBlockingMessage {
    @Override
    public byte getMessageId() {
        return 0;
    }

    @Override
    public void parse(byte[] bytes) throws AntException {

    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public byte getResponseMessageId() {
        return 0;
    }

}

package be.glever.ant.message.control;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class RequestMessage extends AbstractAntMessage implements AntBlockingMessage {

    public static final byte MSG_ID = 0x4d;

    private byte[] bytes;

    public RequestMessage() {
    }

    public RequestMessage(byte channelNumberOrSubMessageId, byte msgIdRequested) {
        this(channelNumberOrSubMessageId, msgIdRequested, null, null);
    }

    /**
     * @param channelNumberOrSubMessageId
     * @param msgIdRequested
     * @param addr                        only used when reading NVM message
     * @param size                        only used when reading NVM message
     */
    public RequestMessage(byte channelNumberOrSubMessageId, byte msgIdRequested, Byte addr, Byte size) {
        this.bytes = new ByteArrayBuilder().write(channelNumberOrSubMessageId, msgIdRequested).writeIfNotNull(addr)
                .writeIfNotNull(size).toByteArray();
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte[] getMessageContent() {
        return this.bytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.bytes = messageContentBytes;
    }

    public byte getMsgIdRequested() {
        return bytes[1];
    }

    @Override
    public byte getResponseMessageId() {
        return getMsgIdRequested();
    }

    public static RequestMessage forMessageId(byte requestedMsgId) {
        return new RequestMessage((byte) 0, requestedMsgId, (byte) 0, (byte) 0);
    }
}
